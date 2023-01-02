package com.andela.irrigation_system.job;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static java.time.Instant.now;
import static java.util.Collections.singletonMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchJobLauncher {
    private final ApplicationContext context;
    private final JobLauncher jobLauncher;

    public BatchStatus launch(String batchJobName) {
        if (!context.containsBean(batchJobName)) {
            log.error("Batch job with [{}] name is not found", batchJobName);
            return BatchStatus.UNKNOWN;
        }
        Job job = context.getBean(batchJobName, Job.class);
        JobExecution execution = run(job).orElseThrow(IllegalStateException::new);
        return execution.getStatus();
    }

    private Optional<JobExecution> run(Job job) {
        Map<String, JobParameter<?>> parameters = singletonMap("t", new JobParameter<>(getTimestampWithZeroMillis(), Long.class));
        try {
            return of(jobLauncher.run(job, new JobParameters(parameters)));
        } catch (JobExecutionException e) {
            log.error("Failed to start job", e);
        }
        return empty();
    }

    private long getTimestampWithZeroMillis() {
        return SECONDS.toMillis(now().getEpochSecond());
    }
}

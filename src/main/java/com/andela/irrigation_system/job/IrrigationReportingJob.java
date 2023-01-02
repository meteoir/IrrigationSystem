package com.andela.irrigation_system.job;


import com.andela.irrigation_system.services.IrrigationService;
import lombok.RequiredArgsConstructor;
import com.andela.irrigation_system.persistence.entity.IrrigationConfig;
import com.andela.irrigation_system.services.ReportingService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.GroupAwareJob;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class IrrigationReportingJob {

    private static final String JOB_NAME = "prepareIrrigationReport";

    @SuppressWarnings("SpringJavaAutowiringInspection")
    private final EntityManagerFactory entityManagerFactory;

    private final IrrigationService trackingService;
    private final ReportingService reportingService;

    @Bean(JOB_NAME)
    public Job reportTimeLogsJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        JobBuilder builder = new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer());
        return new GroupAwareJob(builder
                .start(prepareWeeklyReportStep(jobRepository, transactionManager))
                .next(cleanupExpiredLogsStep(jobRepository, transactionManager))
                .build());
    }

    @Bean
    public Step prepareWeeklyReportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        JpaPagingItemReader<IrrigationConfig> reader = new JpaPagingItemReader<>();
        String query = ""
                + "SELECT t FROM IrrigationConfig t "
                + "WHERE t.createdAt > :date"
                + " AND 1=1 "
                + "ORDER BY t.id ";
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(query);
        reader.setParameterValues(Map.of("createdAt", Instant.now().minus(7, ChronoUnit.DAYS)));
        return new StepBuilder("prepareWeeklyReportStep", jobRepository)
                .<IrrigationConfig, IrrigationConfig>chunk(50, transactionManager)
                .reader(reader)
                .writer(reportingService::prepareReport)
                .build();
    }

    @Bean
    public Step cleanupExpiredLogsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        JpaPagingItemReader<IrrigationConfig> reader = new JpaPagingItemReader<>();
        String query = ""
                + "SELECT t FROM IrrigationConfig t "
                + "WHERE t.createdAt < :date"
                + " AND 1=1 "
                + "ORDER BY t.id ";
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(query);
        reader.setParameterValues(Map.of("createdAt", Instant.now().minus(365, ChronoUnit.DAYS)));
        return new StepBuilder("cleanupExpiredLogsStep", jobRepository)
                .<IrrigationConfig, Long>chunk(500, transactionManager)
                .reader(reader)
                .writer(articleIds -> articleIds.forEach(trackingService::deleteById))
                .taskExecutor(cleanupTaskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor cleanupTaskExecutor() {
        return new SimpleAsyncTaskExecutor("cleanupExecutor");
    }
}


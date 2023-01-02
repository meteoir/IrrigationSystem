package com.andela.irrigation_system.job;

import com.andela.irrigation_system.persistence.entity.IrrigationConfig;
import com.andela.irrigation_system.services.IrrigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig implements SchedulingConfigurer {
    private final Environment env;
    private final IrrigationService irrigationService;


    @Bean
    public Executor scheduledTaskExecutor() {
        return Executors.newScheduledThreadPool(5);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduledTaskExecutor());
        taskRegistrar.addTriggerTask(
                () -> irrigationService.getPaginatedTasks(0, 10, "createdAt", 1L, null),
                triggerContext -> {
                    Calendar nextExecutionTime =  new GregorianCalendar();
                    Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
                    nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
                    nextExecutionTime.add(Calendar.MILLISECOND, env.getProperty("myRate", Integer.class, 10_000));
                    return nextExecutionTime.getTime().toInstant();
                }
        );
        taskRegistrar.addTriggerTask(
                irrigationService::irrigate,
                triggerContext -> {
                    Calendar nextExecutionTime =  new GregorianCalendar();
                    Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
                    nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
                    nextExecutionTime.add(Calendar.MILLISECOND, env.getProperty("fixedSensorRate", Integer.class, 10_000));
                    return nextExecutionTime.getTime().toInstant();
                }
        );
    }
}

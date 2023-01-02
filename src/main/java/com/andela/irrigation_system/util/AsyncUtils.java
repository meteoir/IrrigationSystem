package com.andela.irrigation_system.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@UtilityClass
public class AsyncUtils {
    public static TaskExecutor createAsyncTaskExecutor(int maxThreads) {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(maxThreads);
        return taskExecutor;
    }
}

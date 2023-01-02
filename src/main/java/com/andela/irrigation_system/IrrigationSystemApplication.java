package com.andela.irrigation_system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@Slf4j
@EnableRetry
@EnableBatchProcessing
@SpringBootApplication
public class IrrigationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrrigationSystemApplication.class, args);
        log.info("Irrigation system service started");
    }
}
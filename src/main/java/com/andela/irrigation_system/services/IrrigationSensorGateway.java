package com.andela.irrigation_system.services;

import com.andela.irrigation_system.model.dto.PlotOfLandDto;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class IrrigationSensorGateway {

    @Retryable
    public void irrigate(PlotOfLandDto conf) {
        System.out.println("Stub for sensor gateway, irrigation for area: " + conf.getName());
    }
}

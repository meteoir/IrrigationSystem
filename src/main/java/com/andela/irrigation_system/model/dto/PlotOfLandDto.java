package com.andela.irrigation_system.model.dto;

import com.andela.irrigation_system.persistence.entity.IrrigationZone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlotOfLandDto {
    private Long id;
    private String name;
    private BigDecimal waterAmount;
    private String cron;
    private IrrigationZone irrigationZone;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}

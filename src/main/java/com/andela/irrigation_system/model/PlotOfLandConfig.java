package com.andela.irrigation_system.model;

import com.andela.irrigation_system.model.dto.GpsCoordinate;
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
public class PlotOfLandConfig {
	private Long id;
	private Long userId;
	private String name;
	private List<GpsCoordinate> coordinates;
	private BigDecimal waterAmount;
	private String cron;
	private Integer minutes;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
}
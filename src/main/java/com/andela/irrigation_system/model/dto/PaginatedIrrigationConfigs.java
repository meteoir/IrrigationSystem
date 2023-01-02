package com.andela.irrigation_system.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class PaginatedIrrigationConfigs {
    private Long total;
    @Singular
    private List<PlotOfLandDto> items;
}

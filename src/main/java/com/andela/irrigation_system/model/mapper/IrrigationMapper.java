package com.andela.irrigation_system.model.mapper;

import com.andela.irrigation_system.model.PlotOfLandConfig;
import com.andela.irrigation_system.model.dto.PlotOfLandDto;
import com.andela.irrigation_system.persistence.entity.IrrigationConfig;
import com.andela.irrigation_system.persistence.entity.IrrigationZone;
import com.andela.irrigation_system.persistence.entity.PlotOfLand;
import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.springframework.beans.BeanUtils.copyProperties;

@UtilityClass
public class IrrigationMapper {

    public static IrrigationConfig toEntity(PlotOfLandConfig request) {
        return IrrigationConfig.builder()
                .plotOfLand(new PlotOfLand()
                        .setName(request.getName())
                        .setIrrigationZone(new IrrigationZone().setCoordinates(request.getCoordinates())))
                .cron(request.getCron())
                .waterAmount(request.getWaterAmount())
                .minutes(request.getMinutes())
                .build();
    }

    //todo: use MapStruct\ModelMapper
    public static PlotOfLandDto toDto(IrrigationConfig entry) {
        PlotOfLandDto dto = PlotOfLandDto.builder().build();
        copyProperties(entry, dto);
        dto.setId(entry.getId());
        dto.setName(entry.getPlotOfLand().getName());
        dto.setIrrigationZone(entry.getPlotOfLand().getIrrigationZone());
        dto.setCreatedAt(entry.getCreatedAt().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        dto.setLastUpdatedAt(entry.getCreatedAt().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        return dto;
    }
}

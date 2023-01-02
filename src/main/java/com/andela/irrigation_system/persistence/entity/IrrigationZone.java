package com.andela.irrigation_system.persistence.entity;

import com.andela.irrigation_system.model.dto.GpsCoordinate;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class IrrigationZone {
    @Singular
    List<GpsCoordinate> coordinates;
}

package com.andela.irrigation_system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpsCoordinate {
    private Float latitude;
    private Float longitude;

    @JsonIgnore
    public Optional<GpsCoordinate> getGeo() {
        return Stream.of(latitude, longitude).allMatch(Objects::nonNull)
                ? Optional.of(this)
                : Optional.empty();
    }
}

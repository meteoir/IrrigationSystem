package com.andela.irrigation_system.model.mapper;

import com.andela.irrigation_system.model.dto.UserDto;
import lombok.experimental.UtilityClass;
import com.andela.irrigation_system.model.UserForm;
import com.andela.irrigation_system.persistence.entity.User;

import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.springframework.beans.BeanUtils.copyProperties;

@UtilityClass
public class UserMapper {

    public User toEntity(UserForm request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(request.getRole())
                .status(request.getStatus())
                .multiplier(request.getMultiplier())
                .build();
    }

    public UserDto toDto(User user) {
        UserDto dto = UserDto.builder().build();
        copyProperties(user, dto);
        dto.setId(user.getId());
        dto.setCreatedAt(user.getCreatedAt().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        dto.setLastUpdatedAt(user.getCreatedAt().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        return dto;
    }
}

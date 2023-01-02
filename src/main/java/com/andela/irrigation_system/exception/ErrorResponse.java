package com.andela.irrigation_system.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class ErrorResponse {
    private String userMessage;
    private String errorCode;

    public static ErrorResponse withMessage(String message) {
        return ErrorResponse.builder().userMessage(message).build();
    }
}
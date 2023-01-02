package com.andela.irrigation_system.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class AuthException extends RuntimeException {
    private final String userMessage;
    private final String errorCode;
    private final HttpStatus status;
}

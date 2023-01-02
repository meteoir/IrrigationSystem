package com.andela.irrigation_system.controller;

import com.andela.irrigation_system.config.Permissions;
import com.andela.irrigation_system.model.UserForm;
import com.andela.irrigation_system.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Collections.singleton;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<String> issueToken(@RequestBody UserForm form) {
        return ResponseEntity.ok(authService.issueAccessToken(form, singleton(Permissions.IRRIGATION_ADMIN)));
    }
}

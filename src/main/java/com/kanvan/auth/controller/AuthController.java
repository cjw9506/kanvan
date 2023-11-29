package com.kanvan.auth.controller;

import com.kanvan.auth.dto.UserLoginRequest;
import com.kanvan.auth.dto.AuthenticationResponse;
import com.kanvan.auth.dto.UserSignupRequest;
import com.kanvan.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@Valid @RequestBody UserSignupRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody UserLoginRequest request) {

        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}

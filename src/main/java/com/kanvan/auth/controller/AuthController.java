package com.kanvan.auth.controller;

import com.kanvan.auth.dto.UserLoginRequest;
import com.kanvan.auth.dto.AuthenticationResponse;
import com.kanvan.auth.dto.UserSignupRequest;
import com.kanvan.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@Valid @RequestBody UserSignupRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody UserLoginRequest request) {

        return ResponseEntity.ok(authService.authenticate(request));
    }

}

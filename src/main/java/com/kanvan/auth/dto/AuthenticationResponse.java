package com.kanvan.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원가입 및 로그인 응답")
@Getter
@NoArgsConstructor
public class AuthenticationResponse {

    @Schema(description = "jwt")
    private String token;

    @Builder
    public AuthenticationResponse(String token) {
        this.token = token;
    }
}

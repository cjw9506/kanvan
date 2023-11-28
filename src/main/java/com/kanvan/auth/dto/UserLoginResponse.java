package com.kanvan.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponse {

    private String Bearer;

    @Builder
    public UserLoginResponse(String bearer) {
        Bearer = bearer;
    }
}

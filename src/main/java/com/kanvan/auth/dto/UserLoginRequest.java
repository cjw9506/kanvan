package com.kanvan.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserLoginRequest {

    @NotBlank(message = "계정은 필수입니다.")
    @Length(min = 6, max = 20)
    private String account;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Length(min = 8, max = 20)
    private String password;

}

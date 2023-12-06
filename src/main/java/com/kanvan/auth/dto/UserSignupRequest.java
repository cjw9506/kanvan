package com.kanvan.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "회원가입 요청")
@Getter
@NoArgsConstructor
public class UserSignupRequest {

    @Schema(description = "계정")
    @NotBlank(message = "계정은 필수입니다.")
    @Length(min = 6, max = 20)
    private String account;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Length(min = 8, max = 20)
    private String password;

    @Schema(description = "이름")
    @NotBlank(message = "이름은 필수입니다.")
    @Length(min = 2)
    private String username;

    @Builder
    public UserSignupRequest(String account, String password, String username) {
        this.account = account;
        this.password = password;
        this.username = username;
    }
}

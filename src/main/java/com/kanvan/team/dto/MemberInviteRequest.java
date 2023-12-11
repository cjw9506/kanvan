package com.kanvan.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "유저 초대 요청")
@Getter
@NoArgsConstructor
public class MemberInviteRequest {

    @Schema(description = "초대할 회원 계정")
    @NotBlank(message = "초대할 회원 계정은 필수입니다.")
    @Length(min = 2, max = 10)
    private String account;

    @Builder
    public MemberInviteRequest(String account) {
        this.account = account;
    }
}

package com.kanvan.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class MemberInviteRequest {

    @NotBlank(message = "초대할 회원 계정은 필수입니다.")
    @Length(min = 2, max = 10)
    private String account;

    @NotNull(message = "팀 정보는 필수입니다.")
    private Long teamId;
}

package com.kanvan.team.dto;

import com.kanvan.team.domain.Invite;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberInviteDecideRequest {

    @NotNull(message = "승락 여부는 필수입니다.")
    private Invite invite;

}

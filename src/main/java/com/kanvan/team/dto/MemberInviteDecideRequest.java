package com.kanvan.team.dto;

import com.kanvan.team.domain.Invite;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(description = "초대 의사결졍 요청")
@Getter
public class MemberInviteDecideRequest {

    @Schema(description = "초대 의사결정")
    @NotNull(message = "승락 여부는 필수입니다.")
    private Invite invite;

}

package com.kanvan.team.dto;

import com.kanvan.team.domain.Invite;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "초대 의사결졍 요청")
@Getter
@NoArgsConstructor
public class MemberInviteDecideRequest {

    @Schema(description = "초대 의사결정")
    @NotNull(message = "승락 여부는 필수입니다.")
    private Invite invite;

    @Builder
    public MemberInviteDecideRequest(Invite invite) {
        this.invite = invite;
    }
}

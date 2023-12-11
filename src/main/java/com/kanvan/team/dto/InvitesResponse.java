package com.kanvan.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvitesResponse {

    private Long inviteId;
    private String teamName;

    @Builder
    public InvitesResponse(Long inviteId, String teamName) {
        this.inviteId = inviteId;
        this.teamName = teamName;
    }
}

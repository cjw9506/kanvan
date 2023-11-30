package com.kanvan.team.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvitesResponse {

    private Long inviteId;
    private String teamName;

    public InvitesResponse(Long inviteId, String teamName) {
        this.inviteId = inviteId;
        this.teamName = teamName;
    }
}

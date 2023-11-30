package com.kanvan.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamResponse {

    private Long teamId;
    private String teamName;

    @Builder
    public TeamResponse(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }
}

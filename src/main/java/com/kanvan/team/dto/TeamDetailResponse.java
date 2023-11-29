package com.kanvan.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TeamDetailResponse {

    private Long teamId;
    private String teamName;
    private List<TeamMemberResponse> members;

    @Builder
    public TeamDetailResponse(Long teamId, String teamName, List<TeamMemberResponse> members) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.members = members;

    }
}

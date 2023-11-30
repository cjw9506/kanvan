package com.kanvan.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TeamsResponse {

    private List<TeamResponse> teams;

    @Builder
    public TeamsResponse(List<TeamResponse> teams) {
        this.teams = teams;
    }
}

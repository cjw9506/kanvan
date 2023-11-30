package com.kanvan.team.dto;

import com.kanvan.team.domain.TeamRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamMemberResponse {

    private Long userId;
    private TeamRole role;
    private String username;

    @Builder
    public TeamMemberResponse(Long userId, TeamRole role, String username) {
        this.userId = userId;
        this.role = role;
        this.username = username;
    }
}

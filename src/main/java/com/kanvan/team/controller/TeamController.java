package com.kanvan.team.controller;

import com.kanvan.team.dto.*;
import com.kanvan.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/teams")
    public ResponseEntity<?> create(@Valid @RequestBody TeamCreateRequest request,
                                    Authentication authentication) {
        teamService.create(request, authentication);

        return ResponseEntity.status(CREATED).body(null);
    }

    @PostMapping("/teams/{teamId}/invites")
    @PreAuthorize("hasAuthority(#teamId + '_LEADER')")
    public ResponseEntity<?> invite(@PathVariable(name = "teamId") Long teamId,
                                    @Valid @RequestBody MemberInviteRequest request) {
        teamService.invite(teamId, request);

        return ResponseEntity.status(OK).body(null);
    }

    @PatchMapping("/invites/{inviteId}")
    public ResponseEntity<?> decide(@Valid @RequestBody MemberInviteDecideRequest request,
                                    @PathVariable(name = "inviteId") Long inviteId,
                                    Authentication authentication) {
        teamService.decide(request, inviteId, authentication);

        return ResponseEntity.status(OK).body(null);
    }

    @GetMapping("/teams")
    public ResponseEntity<?> getTeams(Authentication authentication) {
        TeamsResponse response = teamService.getTeams(authentication);

        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/teams/{teamId}")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> getTeam(@PathVariable(name = "teamId") Long teamId) {
        TeamDetailResponse response = teamService.getTeam(teamId);

        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/invite")
    public ResponseEntity getInvites(Authentication authentication) {
        List<InvitesResponse> response = teamService.getInvites(authentication);

        return ResponseEntity.status(OK).body(response);
    }


}
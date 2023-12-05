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
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TeamCreateRequest request,
                                    Authentication authentication) {
        teamService.create(request, authentication);

        return ResponseEntity.status(CREATED).body(null);
    }

    @PostMapping("/{teamId}/invites")
    @PreAuthorize("hasAuthority(#teamId + '_LEADER')")
    public ResponseEntity<?> invite(@PathVariable(name = "teamId") Long teamId,
                                    @Valid @RequestBody MemberInviteRequest request) {
        teamService.invite(teamId, request);

        return ResponseEntity.status(OK).body(null);
    }

    @PatchMapping("/invite/{inviteId}")
    public ResponseEntity<?> decide(@Valid @RequestBody MemberInviteDecideRequest request,
                                    @PathVariable(name = "inviteId") Long inviteId,
                                    Authentication authentication) {
        teamService.decide(request, inviteId, authentication);

        return ResponseEntity.status(OK).body(null);
    }

    @GetMapping
    public ResponseEntity<?> getTeams(Authentication authentication) {
        TeamsResponse response = teamService.getTeams(authentication);

        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeam(@PathVariable(name = "teamId") Long teamId,
                                     Authentication authentication) {
        TeamDetailResponse response = teamService.getTeam(teamId, authentication);

        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/invite")
    public ResponseEntity getInvites(Authentication authentication) {
        List<InvitesResponse> response = teamService.getInvites(authentication);

        return ResponseEntity.status(OK).body(response);
    }


}
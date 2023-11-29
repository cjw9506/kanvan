package com.kanvan.team.controller;

import com.kanvan.team.dto.MemberInviteDecideRequest;
import com.kanvan.team.dto.MemberInviteRequest;
import com.kanvan.team.dto.TeamCreateRequest;
import com.kanvan.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TeamCreateRequest request,
                                    Authentication authentication) {
        teamService.create(request, authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/invite")
    public ResponseEntity<?> invite(@Valid @RequestBody MemberInviteRequest request,
                                    Authentication authentication) {
        teamService.invite(request, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping("/invite/{inviteId}")
    public ResponseEntity<?> invite(@Valid @RequestBody MemberInviteDecideRequest request,
                                    @PathVariable Long inviteId,
                                    Authentication authentication) {
        teamService.decide(request, inviteId, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
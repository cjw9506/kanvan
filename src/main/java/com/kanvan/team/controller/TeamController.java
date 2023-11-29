package com.kanvan.team.controller;

import com.kanvan.team.dto.TeamCreateRequest;
import com.kanvan.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

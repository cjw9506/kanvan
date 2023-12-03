package com.kanvan.ticket.controller;

import com.kanvan.ticket.dto.TicketCreateRequest;
import com.kanvan.ticket.dto.TicketUpdateRequest;
import com.kanvan.ticket.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/teams/{teamId}/columns/{columnId}/tickets")
    public ResponseEntity<?> create(@PathVariable(name = "teamId") Long teamId,
                                    @PathVariable(name = "columnId") Long columnId,
                                    @Valid @RequestBody TicketCreateRequest request,
                                    Authentication authentication) {

        ticketService.create(teamId, columnId, request, authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PatchMapping("/teams/{teamId}/columns/{columnId}/tickets/{ticketId}")
    public ResponseEntity<?> update(@PathVariable(name = "teamId") Long teamId,
                                    @PathVariable(name = "columnId") Long columnId,
                                    @PathVariable(name = "ticketId") Long ticketId,
                                    @Valid @RequestBody TicketUpdateRequest request,
                                    Authentication authentication) {

        ticketService.update(teamId, columnId, ticketId, request, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

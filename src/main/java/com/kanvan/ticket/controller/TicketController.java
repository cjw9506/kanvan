package com.kanvan.ticket.controller;

import com.kanvan.ticket.dto.TicketCreateRequest;
import com.kanvan.ticket.dto.TicketOrderUpdateRequest;
import com.kanvan.ticket.dto.TicketUpdateRequest;
import com.kanvan.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ticket", description = "티켓 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "티켓 생성", description = "티켓 생성")
    @PostMapping("/teams/{teamId}/columns/{columnId}/tickets")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> create(@PathVariable(name = "teamId") Long teamId,
                                    @PathVariable(name = "columnId") int columnOrder,
                                    @Valid @RequestBody TicketCreateRequest request) {

        ticketService.create(teamId, columnOrder, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @Operation(summary = "티켓 필드 변경", description = "티켓 필드 변경")
    @PatchMapping("/teams/{teamId}/columns/{columnId}/tickets/{ticketId}")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> updateFields(@PathVariable(name = "teamId") Long teamId,
                                    @PathVariable(name = "columnId") int columnOrder,
                                    @PathVariable(name = "ticketId") int ticketOrder,
                                    @Valid @RequestBody TicketUpdateRequest request) {

        ticketService.update(teamId, columnOrder, ticketOrder, request);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "티켓 순서 변경", description = "티켓 순서 변경")
    @PatchMapping("/teams/{teamId}/columns/{columnId}/tickets/{ticketId}/order")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> updateOrders(@PathVariable(name = "teamId") Long teamId,
                                          @PathVariable(name = "columnId") int columnOrder,
                                          @PathVariable(name = "ticketId") int ticketOrder,
                                          @Valid @RequestBody TicketOrderUpdateRequest request) {

        ticketService.updateOrders(teamId, columnOrder, ticketOrder, request);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "티켓 삭제", description = "티켓 삭제")
    @DeleteMapping("/teams/{teamId}/columns/{columnId}/tickets/{ticketId}")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> delete(@PathVariable(name = "teamId") Long teamId,
                                    @PathVariable(name = "columnId") int columnOrder,
                                    @PathVariable(name = "ticketId") int ticketOrder) {
        ticketService.delete(teamId, columnOrder, ticketOrder);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}

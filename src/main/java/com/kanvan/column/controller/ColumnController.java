package com.kanvan.column.controller;

import com.kanvan.column.dto.*;
import com.kanvan.column.service.ColumnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    @PostMapping("/teams/{teamId}/columns")
    @PreAuthorize("hasAuthority(#teamId + '_LEADER')")
    public ResponseEntity<?> create(@PathVariable(name = "teamId") Long teamId,
                                    @Valid @RequestBody ColumnCreateRequest request) {

        columnService.create(teamId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/teams/{teamId}/columns")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> getColumns(@PathVariable(name = "teamId") Long teamId) {

        List<ColumnsResponse> response = columnService.getColumns(teamId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/teams/{teamId}/columns/{columnId}")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> updateColumnName(@PathVariable(name = "teamId") Long teamId,
                                              @PathVariable(name = "columnId") int columnOrder,
                                              @Valid @RequestBody ColumnUpdateNameRequest request) {
        columnService.updateColumnName(teamId, columnOrder, request);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping
    public ResponseEntity<?> updateColumnOrder(@Valid @RequestBody ColumnUpdateRequest request,
                                               Authentication authentication) {
        columnService.updateColumnOrder(request, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<?> deleteColumn(@PathVariable(name = "columnId") Long columnId,
                                          @RequestBody ColumnDeleteRequest request,
                                          Authentication authentication) {

        columnService.deleteColumn(columnId, request, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(null);

    }
}

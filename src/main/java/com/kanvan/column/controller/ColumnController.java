package com.kanvan.column.controller;

import com.kanvan.column.dto.ColumnCreateRequest;
import com.kanvan.column.dto.ColumnDeleteRequest;
import com.kanvan.column.dto.ColumnUpdateRequest;
import com.kanvan.column.dto.ColumnsResponse;
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

    //todo 컬럼 api restful하게 바꾸기

    private final ColumnService columnService;

    @PostMapping("/teams/{teamId}/columns")
    @PreAuthorize("hasAuthority(#teamId + '_LEADER')")
    public ResponseEntity<?> create(@PathVariable(name = "teamId") Long teamId,
                                    @Valid @RequestBody ColumnCreateRequest request) {

        columnService.create(teamId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> getColumns(@PathVariable(name = "teamId") Long teamId,
                                        Authentication authentication) {

        List<ColumnsResponse> response = columnService.getColumns(teamId, authentication);

        return ResponseEntity.status(HttpStatus.OK).body(response);
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

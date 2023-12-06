package com.kanvan.column.controller;

import com.kanvan.column.dto.*;
import com.kanvan.column.service.ColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "column", description = "컬럼 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    @Operation(summary = "컬럼 생성", description = "컬럼 생성")
    @PostMapping("/teams/{teamId}/columns")
    @PreAuthorize("hasAuthority(#teamId + '_LEADER')")
    public ResponseEntity<?> create(@PathVariable(name = "teamId") Long teamId,
                                    @Valid @RequestBody ColumnCreateRequest request) {

        columnService.create(teamId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @Operation(summary = "컬럼 목록 조회", description = "컬럼 목록 조회")
    @GetMapping("/teams/{teamId}/columns")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> getColumns(@PathVariable(name = "teamId") Long teamId) {

        List<ColumnsResponse> response = columnService.getColumns(teamId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "컬럼 필드 변경", description = "컬럼 필드 변경")
    @PatchMapping("/teams/{teamId}/columns/{columnId}")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> updateColumnName(@PathVariable(name = "teamId") Long teamId,
                                              @PathVariable(name = "columnId") int columnOrder,
                                              @Valid @RequestBody ColumnUpdateNameRequest request) {
        columnService.updateColumnName(teamId, columnOrder, request);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "컬럼 순서 변경 ", description = "컬럼 순서 변경")
    @PatchMapping("/teams/{teamId}/columns/{columnId}/order")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public ResponseEntity<?> updateColumnOrder(@PathVariable(name = "teamId") Long teamId,
                                               @PathVariable(name = "columnId") int columnOrder,
                                               @Valid @RequestBody ColumnUpdateRequest request) {
        columnService.updateColumnOrder(teamId, columnOrder, request);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "컬럼 삭제", description = "컬럼 삭제")
    @DeleteMapping("/teams/{teamId}/columns/{columnId}")
    @PreAuthorize("hasAuthority(#teamId + '_LEADER')")
    public ResponseEntity<?> deleteColumn(@PathVariable(name = "teamId") Long teamId,
                                          @PathVariable(name = "columnId") int columnOrder) {

        columnService.deleteColumn(teamId, columnOrder);

        return ResponseEntity.status(HttpStatus.OK).body(null);

    }
}

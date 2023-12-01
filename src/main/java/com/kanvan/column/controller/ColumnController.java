package com.kanvan.column.controller;

import com.kanvan.column.dto.ColumnCreateRequest;
import com.kanvan.column.service.ColumnService;
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
@RequestMapping("/api/columns")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ColumnCreateRequest request,
                                    Authentication authentication) {

        columnService.create(request, authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }


}

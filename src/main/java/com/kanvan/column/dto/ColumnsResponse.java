package com.kanvan.column.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ColumnsResponse {

    private Long columnId;
    private String name;
    private int columnOrder;

    @Builder
    public ColumnsResponse(Long columnId, String name, int columnOrder) {
        this.columnId = columnId;
        this.name = name;
        this.columnOrder = columnOrder;
    }
}

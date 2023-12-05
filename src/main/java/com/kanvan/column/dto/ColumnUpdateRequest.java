package com.kanvan.column.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class ColumnUpdateRequest {

    @Min(value = 1, message = "변경할 순서는 필수입니다.")
    private int columnOrder;
}

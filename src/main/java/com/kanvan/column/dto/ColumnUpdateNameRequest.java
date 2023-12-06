package com.kanvan.column.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "컬럼 이름 변경 요청")
@Getter
public class ColumnUpdateNameRequest {

    @Schema(description = "이름")
    private String name;
}

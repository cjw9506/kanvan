package com.kanvan.column.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "컬럼 생성 요청")
@Getter
@NoArgsConstructor
public class ColumnCreateRequest {

    @Schema(description = "이름")
    private String name;

    @Builder
    public ColumnCreateRequest(String name) {
        this.name = name;
    }
}

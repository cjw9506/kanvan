package com.kanvan.column.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Schema(description = "컬럼 순서 변경 요청")
@Getter
public class ColumnUpdateRequest {

    @Schema(description = "순서")
    @Min(value = 1, message = "변경할 순서는 필수입니다.")
    private int columnOrder;
}

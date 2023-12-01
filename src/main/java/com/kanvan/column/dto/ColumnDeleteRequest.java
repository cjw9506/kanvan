package com.kanvan.column.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ColumnDeleteRequest {

    @NotNull(message = "팀 id는 필수입니다.")
    private Long teamId;
}

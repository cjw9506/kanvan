package com.kanvan.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "티켓 순서 변경 요청")
@Getter
public class TicketOrderUpdateRequest {

    @Schema(description = "팀 내 컬럼 순서")
    private int columnId;

    @Schema(description = "컬럼 내 티켓 순서")
    private int ticketOrder;
}

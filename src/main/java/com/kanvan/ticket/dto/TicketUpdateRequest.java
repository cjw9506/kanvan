package com.kanvan.ticket.dto;

import com.kanvan.ticket.domain.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "티켓 필드 변경 요청")
@Getter
public class TicketUpdateRequest {

    @Schema(description = "제목")
    private String title;

    @Schema(description = "작업 태그")
    private Tag tag;

    @Schema(description = "작업 시간")
    private String workingTime;

    @Schema(description = "작업 기한")
    private String deadline;

    @Schema(description = "작업할 회원 계정")
    @NotBlank(message = "계정은 필수입니다.")
    private String memberAccount;
}

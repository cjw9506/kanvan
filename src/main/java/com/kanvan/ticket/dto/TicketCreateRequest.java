package com.kanvan.ticket.dto;

import com.kanvan.ticket.domain.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "티켓 생성 요청")
@Getter
@NoArgsConstructor
public class TicketCreateRequest {

    @Schema(description = "제목")
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @Schema(description = "작업태그")
    @NotNull(message = "작업태그는 필수입니다.")
    private Tag tag;

    @Schema(description = "작업 시간")
    private String workingTime;

    @Schema(description = "작업 기한")
    private String deadline;

    @Schema(description = "작업할 회원 계정")
    private String memberAccount;

    @Builder
    public TicketCreateRequest(String title, Tag tag, String workingTime,
                               String deadline, String memberAccount) {
        this.title = title;
        this.tag = tag;
        this.workingTime = workingTime;
        this.deadline = deadline;
        this.memberAccount = memberAccount;
    }
}
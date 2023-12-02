package com.kanvan.ticket.dto;

import com.kanvan.ticket.domain.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TicketCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotNull(message = "작업태그는 필수입니다.")
    private Tag tag;

    private String workingTime;

    private String deadline;

    private String memberAccount;


}
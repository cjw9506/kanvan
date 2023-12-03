package com.kanvan.ticket.dto;

import com.kanvan.ticket.domain.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TicketUpdateRequest {

    private String title;

    private Tag tag;

    private String workingTime;

    private String deadline;

    @NotBlank(message = "계정은 필수입니다.")
    private String memberAccount;
}

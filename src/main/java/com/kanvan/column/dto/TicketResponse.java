package com.kanvan.column.dto;

import com.kanvan.ticket.domain.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TicketResponse {

    private int TicketOrder;
    private String title;
    private Tag tag;
    private String workingTime;
    private String deadline;
    private String memberAccount;

    public TicketResponse(int ticketOrder, String title, Tag tag, String workingTime,
                          String deadline, String memberAccount) {
        TicketOrder = ticketOrder;
        this.title = title;
        this.tag = tag;
        this.workingTime = workingTime;
        this.deadline = deadline;
        this.memberAccount = memberAccount;
    }
}

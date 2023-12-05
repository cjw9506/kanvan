package com.kanvan.column.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class ColumnsResponse {

    private Long columnId;
    private String name;
    private int columnOrder;
    private List<TicketResponse> tickets;

    @Builder
    public ColumnsResponse(Long columnId, String name, int columnOrder, List<TicketResponse> tickets) {
        this.columnId = columnId;
        this.name = name;
        this.columnOrder = columnOrder;
        this.tickets = tickets;
    }
}

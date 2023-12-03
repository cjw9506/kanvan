package com.kanvan.ticket.domain;

import com.kanvan.column.domain.Columns;
import com.kanvan.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ticketOrder;

    private String title;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    private String workingTime;

    private String deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    private Columns column;

    @Builder
    public Ticket(int ticketOrder, String title, Tag tag, String workingTime,
                  String deadline, User manager, Columns column) {
        this.ticketOrder = ticketOrder;
        this.title = title;
        this.tag = tag;
        this.workingTime = workingTime;
        this.deadline = deadline;
        this.manager = manager;
        this.column = column;
    }

    public void update(String title, Tag tag, String workingTime, String deadline, User manager) {
        this.title = title != null ? title : this.title;
        this.tag = tag != null ? tag : this.tag;
        this.workingTime = workingTime != null ? workingTime : this.workingTime;
        this.deadline = deadline != null ? deadline : this.deadline;
        this.manager = manager != null ? manager : this.manager;
    }
}

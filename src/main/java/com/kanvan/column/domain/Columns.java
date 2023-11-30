package com.kanvan.column.domain;

import com.kanvan.team.domain.Team;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
public class Columns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //순서
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Builder
    public Columns(String name, int order, Team team) {
        this.name = name;
        this.order = order;
        this.team = team;
    }
}

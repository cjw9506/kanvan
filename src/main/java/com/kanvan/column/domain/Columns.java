package com.kanvan.column.domain;

import com.kanvan.team.domain.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Columns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //순서
    private int columnOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Builder
    public Columns(String name, int columnOrder, Team team) {
        this.name = name;
        this.columnOrder = columnOrder;
        this.team = team;
    }
}

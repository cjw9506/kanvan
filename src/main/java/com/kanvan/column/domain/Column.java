package com.kanvan.column.domain;

import com.kanvan.team.domain.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Column {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //순서
    private String order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Builder
    public Column(String name, String order, Team team) {
        this.name = name;
        this.order = order;
        this.team = team;
    }
}

package com.kanvan.column.repository;

import com.kanvan.column.domain.Columns;
import com.kanvan.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

    List<Columns> findByTeam(Team team);

    List<Columns> findByTeamOrderByColumnOrder(Team team);
}

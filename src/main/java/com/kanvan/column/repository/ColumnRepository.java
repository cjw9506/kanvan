package com.kanvan.column.repository;

import com.kanvan.column.domain.Columns;
import com.kanvan.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

    List<Columns> findByTeam(Team team);

    List<Columns> findByTeamOrderByColumnOrder(Team team);

    List<Columns> findByColumnOrderBetween(int min, int max);

    List<Columns> findByColumnOrderGreaterThan(int columnOrder);

    Optional<Columns> findColumnsByIdAndTeamId(Long columnId, Long teamId);

    Optional<Columns> findByTeamAndColumnOrder(Team team, int columnOrder);

}

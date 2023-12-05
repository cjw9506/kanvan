package com.kanvan.column.service;

import com.kanvan.column.domain.Columns;
import com.kanvan.column.dto.*;
import com.kanvan.column.repository.ColumnRepository;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.team.domain.Team;
import com.kanvan.team.repository.TeamRepository;
import com.kanvan.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final TeamRepository teamRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void create(Long teamId, ColumnCreateRequest request) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        int order = columnRepository.findByTeam(team).size() + 1;

        Columns column = Columns.builder()
                .name(request.getName())
                .columnOrder(order)
                .team(team)
                .build();

        columnRepository.save(column);

    }

    public List<ColumnsResponse> getColumns(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        List<Columns> columns = columnRepository.findByTeamOrderByColumnOrder(team);

        return columns.stream()
                .map(column -> {
                    List<TicketResponse> tickets = ticketRepository.findByColumn(column).stream()
                            .map(ticket -> new TicketResponse(ticket.getTicketOrder(),
                                    ticket.getTitle(), ticket.getTag(), ticket.getWorkingTime(),
                                    ticket.getDeadline(), ticket.getManager().getUsername()))
                            .collect(Collectors.toList());
                    return new ColumnsResponse(column.getId(), column.getName(), column.getColumnOrder(),
                            tickets);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateColumnName(Long teamId, int columnOrder, ColumnUpdateNameRequest request) {

        Columns column = columnRepository.findByTeamIdAndColumnOrder(teamId, columnOrder).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        column.updateColumnName(request.getName());
    }


    @Transactional
    public void updateColumnOrder(Long teamId, int columnOrder, ColumnUpdateRequest request) {

        Columns column = columnRepository.findByTeamIdAndColumnOrder(teamId, columnOrder).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        int min = Math.min(column.getColumnOrder(), request.getColumnOrder());
        int max = Math.max(column.getColumnOrder(), request.getColumnOrder());

        List<Columns> columns = columnRepository.findByColumnOrderBetween(min, max);

        int offset = column.getColumnOrder() < request.getColumnOrder() ? -1 : 1;

        if (offset == 1) {
            columns.forEach(col -> {
                if (col.getColumnOrder() == column.getColumnOrder()) {
                    col.updateColumnOrder(request.getColumnOrder());
                } else {
                    col.updateColumnOrder(col.getColumnOrder() + offset);
                }
            });

        } else {
            Collections.reverse(columns);
            columns.forEach(col -> {
                if (col.getColumnOrder() == column.getColumnOrder()) {
                    col.updateColumnOrder(request.getColumnOrder());
                } else {
                    col.updateColumnOrder(col.getColumnOrder() + offset);
                }
            });
        }


    }

    @Transactional
    public void deleteColumn(Long teamId, int columnOrder) {

        Columns column = columnRepository.findByTeamIdAndColumnOrder(teamId, columnOrder).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        if (ticketRepository.findByColumn(column).isEmpty()) {
            columnRepository.delete(column);
            List<Columns> columns = columnRepository.findByColumnOrderGreaterThan(column.getColumnOrder());
            columns.forEach(col -> col.updateColumnOrder(col.getColumnOrder() - 1));

        } else {
            throw new CustomException(ErrorCode.COLUMN_NOT_EMPTY);
        }
    }


}

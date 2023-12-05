package com.kanvan.column.service;

import com.kanvan.column.domain.Columns;
import com.kanvan.column.dto.*;
import com.kanvan.column.repository.ColumnRepository;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.team.domain.Member;
import com.kanvan.team.domain.Team;
import com.kanvan.team.domain.TeamRole;
import com.kanvan.team.repository.MemberRepository;
import com.kanvan.team.repository.TeamRepository;
import com.kanvan.ticket.repository.TicketRepository;
import com.kanvan.user.domain.User;
import com.kanvan.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
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
    public void updateColumnOrder(ColumnUpdateRequest request, Authentication authentication) {
        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Team team = teamRepository.findById(request.getTeamId()).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        Member member = memberRepository.findByMemberAndTeam(user, team).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        int min = Math.min(request.getCurrentColumnOrder(), request.getFutureColumnOrder());
        int max = Math.max(request.getCurrentColumnOrder(), request.getFutureColumnOrder());

        List<Columns> columns = columnRepository.findByColumnOrderBetween(min, max);

        int offset = request.getCurrentColumnOrder() < request.getFutureColumnOrder() ? -1 : 1;

        columns.forEach(column -> {
            if (column.getColumnOrder() == request.getCurrentColumnOrder()) {
                column.updateColumnOrder(request.getFutureColumnOrder());
            } else {
                column.updateColumnOrder(column.getColumnOrder() + offset);
            }
        });
    }

    @Transactional
    public void deleteColumn(Long columnId, ColumnDeleteRequest request, Authentication authentication) {
        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Team team = teamRepository.findById(request.getTeamId()).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        Member member = memberRepository.findByMemberAndTeam(user, team).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() != TeamRole.LEADER) throw new CustomException(ErrorCode.MEMBER_NOT_LEADER);

        Columns columnToBeDeleted = columnRepository.findById(columnId).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        List<Columns> columns = columnRepository.findByColumnOrderGreaterThan(columnToBeDeleted.getColumnOrder());

        columns.forEach(column -> column.updateColumnOrder(column.getColumnOrder() - 1));

        columnRepository.delete(columnToBeDeleted);
    }
}

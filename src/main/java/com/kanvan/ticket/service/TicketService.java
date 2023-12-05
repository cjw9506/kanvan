package com.kanvan.ticket.service;

import com.kanvan.column.domain.Columns;
import com.kanvan.column.repository.ColumnRepository;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.team.domain.Member;
import com.kanvan.team.domain.Team;
import com.kanvan.team.repository.MemberRepository;
import com.kanvan.team.repository.TeamRepository;
import com.kanvan.ticket.domain.Ticket;
import com.kanvan.ticket.dto.TicketCreateRequest;
import com.kanvan.ticket.dto.TicketOrderUpdateRequest;
import com.kanvan.ticket.dto.TicketUpdateRequest;
import com.kanvan.ticket.repository.TicketRepository;
import com.kanvan.user.domain.User;
import com.kanvan.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void create(Long teamId, int columnId, TicketCreateRequest request) {

        //작업자
        User manager = userRepository.findByAccount(request.getMemberAccount()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //멤버 검증
        Member verifiedMember = memberRepository.findByMemberAndTeamId(manager, teamId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        //컬럼
        Columns column = columnRepository.findByColumnOrderAndTeamId(columnId, teamId).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        int order =  ticketRepository.findByColumn(column).size();

        Ticket ticket = Ticket.builder()
                .ticketOrder(order + 1)
                .title(request.getTitle())
                .tag(request.getTag())
                .workingTime(request.getWorkingTime())
                .deadline(request.getDeadline())
                .manager(manager)
                .column(column)
                .build();

        ticketRepository.save(ticket);

    }

    @Transactional
    public void update(Long teamId, int columnId, int ticketId, TicketUpdateRequest request) {

        //컬럼 먼저 찾고 -> teamId and columnOrder
        Columns column = columnRepository.findByTeamIdAndColumnOrder(teamId, columnId).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
        //티켓 찾고 -> 컬럼의 columnOrder and ticketId
        Ticket ticket = ticketRepository.findByTicketOrderAndColumnId(ticketId, column.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
        //바꿀 유저 찾고
        User changedUser = userRepository.findByAccount(request.getMemberAccount()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
        //바꿀 유저 멤버인지 확인하고
        Member member = memberRepository.findByMemberAndTeamId(changedUser, teamId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        //업데이트
        ticket.update(request.getTitle(), request.getTag(), request.getWorkingTime(),
                request.getDeadline(), changedUser);
    }

    @Transactional
    public void updateOrders(Long teamId, int columnId, int ticketId, TicketOrderUpdateRequest request) {

        Columns column = columnRepository.findByTeamIdAndColumnOrder(teamId, columnId).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        Ticket ticket = ticketRepository.findByTicketOrderAndColumnId(ticketId, column.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        Columns changedColumn = columnRepository.findByTeamIdAndColumnOrder(teamId, request.getColumnId()).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        if (columnId != request.getColumnId()) {
            //case 1 -> 컬럼 변경
            List<Ticket> tickets = ticketRepository.findByColumnAndTicketOrderGreaterThan(column, ticketId);

            tickets.forEach(tk -> {
                System.out.println(tk.getTitle());
                tk.updateTicketOrder(tk.getTicketOrder() - 1);
            });
            List<Ticket> changedColumnTickets = ticketRepository.findByColumnAndTicketOrderGreaterThan(changedColumn, request.getTicketOrder() - 1);
            changedColumnTickets.forEach(tk -> {
                tk.updateTicketOrder(tk.getTicketOrder() + 1);
            });
            ticket.updateTicketOrder(request.getTicketOrder());
            ticket.updateColumn(changedColumn);
        } else {
            //case 2 -> 컬럼 변경 x, 순서만 변경
            int min = Math.min(request.getTicketOrder(), ticket.getTicketOrder());
            int max = Math.max(request.getTicketOrder(), ticket.getTicketOrder());

            List<Ticket> tickets = ticketRepository.findByTicketOrderBetween(min, max);

            int offset = ticket.getTicketOrder() < request.getTicketOrder() ? -1 : 1;

            tickets.forEach(tk -> {
                if (tk.getTicketOrder() == ticketId) {
                    tk.updateTicketOrder(request.getTicketOrder());
                } else {
                    tk.updateTicketOrder(tk.getTicketOrder() + offset);
                }
            });
        }
    }

    @Transactional
    public void delete(String teamName, int columnId, int ticketId, Authentication authentication) {
        Ticket ticket = ticketRepository.findByTicketOrder(ticketId).orElseThrow(
                () -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        Team team = teamRepository.findByTeamName(teamName).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        Columns column = columnRepository.findByTeamAndColumnOrder(team, columnId).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        //===== 권한 확인 =====//
        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Member member = memberRepository.findByMemberAndTeam(user, team).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        //===== 권한 확인 =====//

        ticketRepository.delete(ticket);

        List<Ticket> tickets = ticketRepository.findByColumnAndTicketOrderGreaterThan(column, ticketId);

        tickets.forEach(tk -> {
            tk.updateTicketOrder(tk.getTicketOrder() - 1);
                });
    }
}

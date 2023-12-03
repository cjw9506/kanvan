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
    public void create(Long teamId, Long columnId, TicketCreateRequest request,
                       Authentication authentication) {

        //todo 조회 줄이기 -> if i can

        //작업자
        User manager = userRepository.findByAccount(request.getMemberAccount()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //컬럼
        Columns column = columnRepository.findColumnsByIdAndTeamId(columnId, teamId).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        //===== 여기부터는 권한때문인데 고민해봐야함 =====//
        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Member member = memberRepository.findByMemberAndTeamId(user, teamId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member worker = memberRepository.findByMemberAndTeamId(manager, teamId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        //===== 여기까지는 권한때문인데 고민해봐야함 =====//

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
    public void update(Long teamId, Long columnId, Long ticketId,
                       TicketUpdateRequest request, Authentication authentication) {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        Columns column = columnRepository.findColumnsByIdAndTeamId(columnId, teamId).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Member member = memberRepository.findByMemberAndTeamId(user, teamId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        User changedUser = userRepository.findByAccount(request.getMemberAccount()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ticket.update(request.getTitle(), request.getTag(), request.getWorkingTime(),
                request.getDeadline(), changedUser);
    }

    @Transactional
    public void updateOrders(String teamName, int columnId, int ticketId,
                             TicketOrderUpdateRequest request, Authentication authentication) {

        Ticket ticket = ticketRepository.findByTicketOrder(ticketId).orElseThrow(
                () -> new CustomException(ErrorCode.TICKET_NOT_FOUND));

        Team team = teamRepository.findByTeamName(teamName).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        Columns column = columnRepository.findByTeamAndColumnOrder(team, columnId).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        Columns changedColumn = columnRepository.findByTeamAndColumnOrder(team, request.getColumnId()).orElseThrow(
                () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

        //===== 권한 확인 =====//
        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Member member = memberRepository.findByMemberAndTeam(user, team).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        //===== 권한 확인 =====//

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


        } else {
            //case 2 -> 컬럼 변경 x, 순서만 변경
            int min = Math.min(request.getTicketOrder(), ticket.getTicketOrder());
            int max = Math.max(request.getTicketOrder(), ticket.getTicketOrder());

            List<Ticket> tickets = ticketRepository.findByTicketOrderBetween(min, max);

            int offset = ticket.getTicketOrder() < request.getTicketOrder() ? -1 : 1;
            // 2-> 4번으로
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

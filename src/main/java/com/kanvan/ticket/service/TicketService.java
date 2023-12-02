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
import com.kanvan.ticket.repository.TicketRepository;
import com.kanvan.user.domain.User;
import com.kanvan.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(Long teamId, Long columnId, TicketCreateRequest request,
                       Authentication authentication) {

        //todo 조회 줄이기 -> if i can

        //작업자
        User manager = userRepository.findByAccount(request.getMemberAccount()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //컬럼
        Columns column = columnRepository.findColumnsByIdAndTeamId(teamId, columnId).orElseThrow(
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
}

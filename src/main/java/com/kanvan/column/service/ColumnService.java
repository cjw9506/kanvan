package com.kanvan.column.service;

import com.kanvan.column.domain.Columns;
import com.kanvan.column.dto.ColumnCreateRequest;
import com.kanvan.column.repository.ColumnRepository;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.team.domain.Member;
import com.kanvan.team.domain.Team;
import com.kanvan.team.domain.TeamRole;
import com.kanvan.team.repository.MemberRepository;
import com.kanvan.team.repository.TeamRepository;
import com.kanvan.user.domain.User;
import com.kanvan.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(ColumnCreateRequest request, Authentication authentication) {

        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Team team = teamRepository.findById(request.getTeamId()).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        Member member = memberRepository.findByMemberAndTeam(user, team).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole().equals(TeamRole.LEADER)) throw new CustomException(ErrorCode.MEMBER_NOT_LEADER);

        int order = columnRepository.findByTeam(team).size() + 1;

        Columns column = Columns.builder()
                .name(request.getName())
                .columnOrder(order)
                .team(team)
                .build();

        columnRepository.save(column);

    }
}
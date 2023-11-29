package com.kanvan.team.service;

import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.team.domain.Team;
import com.kanvan.team.domain.Member;
import com.kanvan.team.domain.TeamRole;
import com.kanvan.team.dto.TeamCreateRequest;
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
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(TeamCreateRequest request, Authentication authentication) {

        if (teamRepository.findByTeamName(request.getTeamName()).isPresent()) {
            throw new CustomException(ErrorCode.TEAM_IS_EXIST);
        }

        Team team = Team.builder()
                .teamName(request.getTeamName())
                .build();

        teamRepository.save(team);

        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Member teamMember = Member.builder()
                .team(team)
                .member(user)
                .role(TeamRole.LEADER)
                .build();

        memberRepository.save(teamMember);
    }
}

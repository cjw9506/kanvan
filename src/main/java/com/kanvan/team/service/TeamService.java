package com.kanvan.team.service;

import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.team.domain.Invite;
import com.kanvan.team.domain.Team;
import com.kanvan.team.domain.Member;
import com.kanvan.team.domain.TeamRole;
import com.kanvan.team.dto.*;
import com.kanvan.team.repository.MemberRepository;
import com.kanvan.team.repository.TeamRepository;
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
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(TeamCreateRequest request, Authentication authentication) {

        //팀 이름 중복 검사
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
                .inviteStatus(Invite.UNNECESSARY)
                .build();

        memberRepository.save(teamMember);

        String authority = team.getId() + "_LEADER";

        user.setTeamAuthority(authority);
    }

    @Transactional
    public void invite(Long teamId, MemberInviteRequest request) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        User userToInvite = userRepository.findByAccount(request.getAccount()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 이미 초대되어 있는가?
        if (memberRepository.findByMemberAndTeamId(userToInvite, teamId).isPresent()) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        Member waitingMember = Member.builder()
                .team(team)
                .member(userToInvite)
                .role(TeamRole.MEMBER)
                .inviteStatus(Invite.WAITING)
                .build();

        memberRepository.save(waitingMember);

    }

    @Transactional
    public void decide(MemberInviteDecideRequest request, Long inviteId,
                       Authentication authentication) {

        //수락할 유저
        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //초대 받은 정보
        Member waitingMember = memberRepository.findById(inviteId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (waitingMember.getMember().equals(user) && waitingMember.getInviteStatus() == Invite.WAITING) {
            if (request.getInvite() == Invite.REFUSE) {
                memberRepository.delete(waitingMember);
            } else {
                waitingMember.updateInviteStatus(request.getInvite());

                String authority = waitingMember.getTeam().getId() + "_MEMBER";

                user.setTeamAuthority(authority);
            }
        }
    }

    public TeamsResponse getTeams(Authentication authentication) {

        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Member> teamsOfUser = memberRepository.findByMember(user);

        List<TeamResponse> response = teamsOfUser.stream()
                .map(member -> new TeamResponse(member.getTeam().getId(), member.getTeam().getTeamName()))
                .collect(Collectors.toList());

        return TeamsResponse.builder()
                .teams(response)
                .build();
    }

    public TeamDetailResponse getTeam(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        List<Member> members = memberRepository.findByTeam(team);

        List<TeamMemberResponse> response = members.stream()
                .map(member -> new TeamMemberResponse(member.getMember().getId(),
                        member.getRole(), member.getMember().getUsername()))
                .collect(Collectors.toList());

        return TeamDetailResponse.builder()
                .teamId(teamId)
                .teamName(team.getTeamName())
                .members(response)
                .build();
    }

    public List<InvitesResponse> getInvites(Authentication authentication) {
        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Member> waiting = memberRepository.findByMemberAndInviteStatus(user, Invite.WAITING);

        List<InvitesResponse> response = waiting.stream()
                .map(invite -> new InvitesResponse(invite.getId(), invite.getTeam().getTeamName()))
                .collect(Collectors.toList());

        return response;

    }
}
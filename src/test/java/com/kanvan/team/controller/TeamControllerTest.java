package com.kanvan.team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanvan.auth.filter.JwtAuthenticationFilter;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.team.domain.Invite;
import com.kanvan.team.domain.TeamRole;
import com.kanvan.team.dto.*;
import com.kanvan.team.service.TeamService;
import com.kanvan.user.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TeamController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeamService teamService;

    @DisplayName("팀 생성 성공")
    @WithMockUser
    @Test
    void createTeam() throws Exception {

        TeamCreateRequest request = TeamCreateRequest.builder()
                .teamName("지원이팀")
                .build();

        Authentication authentication = mock(Authentication.class);

        doNothing().when(teamService).create(request, authentication);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/teams").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(teamService).create(any(TeamCreateRequest.class), any(Authentication.class));

    }

    @DisplayName("팀 생성 실패 - 중복 팀명")
    @WithMockUser
    @Test
    void createTeamFailed() throws Exception {

        TeamCreateRequest request = TeamCreateRequest.builder()
                .teamName("지원이팀")
                .build();

        doThrow(new CustomException(ErrorCode.TEAM_IS_EXIST))
                .when(teamService).create(any(TeamCreateRequest.class), any(Authentication.class));

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/teams").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(teamService).create(any(TeamCreateRequest.class), any(Authentication.class));

    }

    @DisplayName("팀원초대 성공")
    @WithMockUser
    @Test
    void inviteUser() throws Exception {

        MemberInviteRequest request = MemberInviteRequest.builder()
                .account("Account1")
                .build();

        doNothing().when(teamService).invite(1L, request);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/teams/1/invites").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(teamService).invite(any(Long.class), any(MemberInviteRequest.class));

    }

    @DisplayName("팀원초대 실패 - validation")
    @WithMockUser
    @Test
    void inviteUserFailed() throws Exception {

        MemberInviteRequest request = MemberInviteRequest.builder()
                .account("Account1234123")
                .build();

        doNothing().when(teamService).invite(1L, request);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/teams/1/invites").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value( "[account] length must be between 2 and 10"));


    }

    @DisplayName("팀원초대 실패 - 없는 계정")
    @WithMockUser
    @Test
    void inviteUserFailed2() throws Exception {

        MemberInviteRequest request = MemberInviteRequest.builder()
                .account("Account1")
                .build();

        doThrow(new CustomException(ErrorCode.USER_NOT_FOUND))
                .when(teamService).invite(any(Long.class), any(MemberInviteRequest.class));

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/teams/1/invites").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @DisplayName("초대 수락")
    @WithMockUser
    @Test
    void acceptInvite() throws Exception {

        MemberInviteDecideRequest request = MemberInviteDecideRequest.builder()
                .invite(Invite.ACCEPT)
                .build();

        Authentication authentication = mock(Authentication.class);

        doNothing().when(teamService).decide(request, 1L, authentication);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/invites/1").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("초대 거절")
    @WithMockUser
    @Test
    void refuseInvite() throws Exception {

        MemberInviteDecideRequest request = MemberInviteDecideRequest.builder()
                .invite(Invite.REFUSE)
                .build();

        Authentication authentication = mock(Authentication.class);

        doNothing().when(teamService).decide(request, 1L, authentication);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/invites/1").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("팀 목록 조회")
    @WithMockUser
    @Test
    void getTeams() throws Exception {

        TeamResponse team1 = TeamResponse.builder()
                .teamId(1L)
                .teamName("지원팀")
                .build();

        TeamResponse team2 = TeamResponse.builder()
                .teamId(2L)
                .teamName("지원팀2")
                .build();

        TeamsResponse response = TeamsResponse.builder()
                .teams(List.of(team1, team2))
                .build();

        when(teamService.getTeams(any(Authentication.class))).thenReturn(response);

        mockMvc.perform(get("/api/teams").with(csrf())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teams[0].teamName").value("지원팀"))
                .andExpect(jsonPath("$.teams[1].teamName").value("지원팀2"));

        verify(teamService).getTeams(any(Authentication.class));

    }

    @DisplayName("팀 단건 조회")
    @WithMockUser
    @Test
    void getTeam() throws Exception {

        TeamMemberResponse leader = TeamMemberResponse.builder()
                .userId(1L)
                .username("정지원")
                .role(TeamRole.LEADER)
                .build();

        TeamMemberResponse member = TeamMemberResponse.builder()
                .userId(2L)
                .username("지원정")
                .role(TeamRole.MEMBER)
                .build();

        TeamDetailResponse response = TeamDetailResponse.builder()
                .teamId(1L)
                .teamName("지원팀")
                .members(List.of(leader, member))
                .build();

        when(teamService.getTeam(1L)).thenReturn(response);

        mockMvc.perform(get("/api/teams/1").with(csrf())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(teamService).getTeam(any(Long.class));

    }






}
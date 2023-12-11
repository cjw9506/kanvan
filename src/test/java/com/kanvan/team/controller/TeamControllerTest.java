package com.kanvan.team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanvan.auth.filter.JwtAuthenticationFilter;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import com.kanvan.team.dto.TeamCreateRequest;
import com.kanvan.team.service.TeamService;
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

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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


}
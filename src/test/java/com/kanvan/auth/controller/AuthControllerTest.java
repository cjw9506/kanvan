package com.kanvan.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanvan.auth.dto.AuthenticationResponse;
import com.kanvan.auth.dto.UserLoginRequest;
import com.kanvan.auth.dto.UserSignupRequest;
import com.kanvan.auth.filter.JwtAuthenticationFilter;
import com.kanvan.auth.service.AuthService;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;


    @DisplayName("회원가입 성공")
    @WithMockUser
    @Test
    void signup() throws Exception {

        UserSignupRequest request = UserSignupRequest.builder()
                .account("testAccount")
                .password("testPassword")
                .username("홍길동")
                .build();

        AuthenticationResponse token = AuthenticationResponse.builder()
                .token("testToken")
                .build();

        when(authService.register(any(UserSignupRequest.class))).thenReturn(token);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/signup").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testToken"));


    }

    @DisplayName("회원가입 실패 - 중복 계정")
    @WithMockUser
    @Test
    void signupfail1() throws Exception {

        UserSignupRequest request = UserSignupRequest.builder()
                .account("testAccount")
                .password("testPassword")
                .username("홍길동")
                .build();

        AuthenticationResponse token = AuthenticationResponse.builder()
                .token("testToken")
                .build();

        when(authService.register(any(UserSignupRequest.class)))
                .thenThrow(new CustomException(ErrorCode.USER_ALREADY_EXIST));

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/signup").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("U001"))
                .andExpect(jsonPath("$.errorMessage").value("이미 계정이 존재합니다."));


    }

    @DisplayName("회원가입 실패 - validation")
    @WithMockUser
    @Test
    void signupfail2() throws Exception {

        UserSignupRequest request = UserSignupRequest.builder()
                .account("test")
                .password("testPassword")
                .username("홍길동")
                .build();

        AuthenticationResponse token = AuthenticationResponse.builder()
                .token("testToken")
                .build();

        when(authService.register(any(UserSignupRequest.class))).thenReturn(token);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/signup").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("[account] length must be between 6 and 20"));

    }

    @DisplayName("로그인 성공")
    @WithMockUser
    @Test
    void login() throws Exception {

        UserLoginRequest request = UserLoginRequest.builder()
                .account("testAccount")
                .password("testPassword")
                .build();

        AuthenticationResponse token = AuthenticationResponse.builder()
                .token("testToken")
                .build();

        when(authService.authenticate(any(UserLoginRequest.class))).thenReturn(token);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/login").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testToken"));
    }
}
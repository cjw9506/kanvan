package com.kanvan.column.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanvan.auth.filter.JwtAuthenticationFilter;
import com.kanvan.column.dto.ColumnCreateRequest;
import com.kanvan.column.service.ColumnService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ColumnController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
class ColumnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ColumnService columnService;

    @DisplayName("컬럼 생성 성공")
    @WithMockUser
    @Test
    void createColumn() throws Exception {


        ColumnCreateRequest request = ColumnCreateRequest.builder()
                .name("Todo")
                .build();

        doNothing().when(columnService).create(1L, request);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/teams/1/columns").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(columnService).create(any(Long.class), any(ColumnCreateRequest.class));

    }


}
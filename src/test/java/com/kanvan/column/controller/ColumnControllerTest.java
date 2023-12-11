package com.kanvan.column.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanvan.auth.filter.JwtAuthenticationFilter;
import com.kanvan.column.dto.ColumnCreateRequest;
import com.kanvan.column.dto.ColumnsResponse;
import com.kanvan.column.dto.TicketResponse;
import com.kanvan.column.service.ColumnService;
import com.kanvan.ticket.domain.Tag;
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

    @DisplayName("컬럼 목록 조회 - 티켓 x")
    @WithMockUser
    @Test
    void getColumns() throws Exception {

        ColumnsResponse column1 = ColumnsResponse.builder()
                .columnId(1L)
                .name("Todo")
                .columnOrder(1)
                .build();

        List<ColumnsResponse> response = List.of(column1);

        when(columnService.getColumns(1L)).thenReturn(response);

        mockMvc.perform(get("/api/teams/1/columns").with(csrf())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(columnService).getColumns(any(Long.class));

    }

    @DisplayName("컬럼 목록 조회 - 티켓 포함")
    @WithMockUser
    @Test
    void getColumns2() throws Exception {

        TicketResponse ticket = TicketResponse.builder()
                .ticketOrder(1)
                .title("column test")
                .tag(Tag.BACKEND)
                .workingTime("3 hours")
                .deadline("oct 12")
                .memberAccount("account1")
                .build();

        ColumnsResponse column1 = ColumnsResponse.builder()
                .columnId(1L)
                .name("Todo")
                .columnOrder(1)
                .tickets(List.of(ticket))
                .build();

        List<ColumnsResponse> response = List.of(column1);

        when(columnService.getColumns(1L)).thenReturn(response);

        mockMvc.perform(get("/api/teams/1/columns").with(csrf())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(columnService).getColumns(any(Long.class));

    }
}
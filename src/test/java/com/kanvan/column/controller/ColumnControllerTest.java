package com.kanvan.column.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanvan.auth.filter.JwtAuthenticationFilter;
import com.kanvan.column.dto.*;
import com.kanvan.column.service.ColumnService;
import com.kanvan.common.exception.CustomException;
import com.kanvan.common.exception.ErrorCode;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @DisplayName("컬럼 이름 수정")
    @WithMockUser
    @Test
    void columnUpdateName() throws Exception {

        ColumnUpdateNameRequest request = ColumnUpdateNameRequest.builder()
                .name("update title")
                .build();

        doNothing().when(columnService).updateColumnName(1L, 1, request);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/teams/1/columns/1").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(columnService).updateColumnName(any(Long.class), any(Integer.class), any(ColumnUpdateNameRequest.class));
    }

    @DisplayName("컬럼 순서 변경")
    @WithMockUser
    @Test
    void columnUpdateOrder() throws Exception {

        ColumnUpdateRequest request = ColumnUpdateRequest.builder()
                .columnOrder(3)
                .build();

        doNothing().when(columnService).updateColumnOrder(1L, 1, request);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/teams/1/columns/1/order").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(columnService).updateColumnOrder(any(Long.class), any(Integer.class), any(ColumnUpdateRequest.class));
    }

    @DisplayName("컬럼 삭제 성공")
    @WithMockUser
    @Test
    void deleteColumn() throws Exception {

        doNothing().when(columnService).deleteColumn(1L, 2);

        mockMvc.perform(delete("/api/teams/1/columns/1").with(csrf())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(columnService).deleteColumn(any(Long.class), any(Integer.class));
    }

    @DisplayName("컬럼 삭제 실패")
    @WithMockUser
    @Test
    void deleteColumnFailed() throws Exception {

        doThrow(new CustomException(ErrorCode.COLUMN_NOT_EMPTY))
                .when(columnService).deleteColumn(1L, 2);

        mockMvc.perform(delete("/api/teams/1/columns/2").with(csrf())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(columnService).deleteColumn(any(Long.class), any(Integer.class));
    }


}
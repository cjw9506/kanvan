package com.kanvan.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanvan.auth.filter.JwtAuthenticationFilter;
import com.kanvan.ticket.domain.Tag;
import com.kanvan.ticket.dto.TicketCreateRequest;
import com.kanvan.ticket.dto.TicketOrderUpdateRequest;
import com.kanvan.ticket.dto.TicketUpdateRequest;
import com.kanvan.ticket.service.TicketService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TicketController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    @DisplayName("티켓 생성 성공")
    @WithMockUser
    @Test
    void createTicket() throws Exception {

        TicketCreateRequest request = TicketCreateRequest.builder()
                .title("로그인 기능 구현")
                .tag(Tag.BACKEND)
                .workingTime("3 hour")
                .deadline("sep 12")
                .memberAccount("account1")
                .build();

        doNothing().when(ticketService).create(1L, 1, request);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/teams/1/columns/1/tickets").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(ticketService).create(any(Long.class), any(Integer.class), any(TicketCreateRequest.class));
    }

    @DisplayName("티켓 필드 수정")
    @WithMockUser
    @Test
    void updateTicketFields() throws Exception {

        TicketUpdateRequest request = TicketUpdateRequest.builder()
                .title("로그인 기능 구현")
                .tag(Tag.BACKEND)
                .workingTime("3 hour")
                .deadline("sep 12")
                .memberAccount("account1")
                .build();

        doNothing().when(ticketService).update(1L, 1, 1, request);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/teams/1/columns/1/tickets/1").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(ticketService).update(any(Long.class), any(Integer.class),
                any(Integer.class), any(TicketUpdateRequest.class));
    }

    @DisplayName("티켓 순서 수정")
    @WithMockUser
    @Test
    void updateOrderFields() throws Exception {

        TicketOrderUpdateRequest request = TicketOrderUpdateRequest.builder()
                .columnId(2)
                .ticketOrder(2)
                .build();

        doNothing().when(ticketService).updateOrders(1L, 1, 1, request);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/teams/1/columns/1/tickets/1/order").with(csrf())
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(ticketService).updateOrders(any(Long.class), any(Integer.class),
                any(Integer.class), any(TicketOrderUpdateRequest.class));
    }

    @DisplayName("티켓 삭제")
    @WithMockUser
    @Test
    void deleteTicket() throws Exception {

        doNothing().when(ticketService).delete(1L, 1, 1);

        mockMvc.perform(delete("/api/teams/1/columns/1/tickets/1").with(csrf())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(ticketService).delete(any(Long.class), any(Integer.class),
                any(Integer.class));
    }

}
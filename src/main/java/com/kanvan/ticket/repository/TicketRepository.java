package com.kanvan.ticket.repository;

import com.kanvan.column.domain.Columns;
import com.kanvan.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByColumn(Columns column);

    Optional<Ticket> findByTicketOrder(int ticketOrder);

    List<Ticket> findByTicketOrderBetween(int min, int max);

    List<Ticket> findByColumnAndTicketOrderGreaterThan(Columns column, int ticketOrder);

    Optional<Ticket> findByTicketOrderAndColumnId(int ticketOrder, Long columnId);
}

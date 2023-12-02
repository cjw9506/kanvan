package com.kanvan.ticket.repository;

import com.kanvan.column.domain.Columns;
import com.kanvan.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByColumn(Columns column);
}

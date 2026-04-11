package com.edts.concert.repository;

import com.edts.concert.entity.TicketSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSlotRepository extends JpaRepository<TicketSlot, Long> {

    List<TicketSlot> findByConcertId(Long concertId);
}

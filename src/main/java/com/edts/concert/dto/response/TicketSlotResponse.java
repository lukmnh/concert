package com.edts.concert.dto.response;

import com.edts.concert.entity.TicketSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketSlotResponse {
    private Long id;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    private int totalTickets;
    private int remainingTickets;
}

package com.edts.concert.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertResponse {
    private Long id;
    private String name;
    private String venue;
    private String description;
    private List<TicketSlotResponse> ticketSlots;
    private LocalDateTime createdAt;
}

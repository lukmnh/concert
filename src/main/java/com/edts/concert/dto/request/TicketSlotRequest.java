package com.edts.concert.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketSlotRequest {
    @NotNull(message = "Sale start time is required")
    @Future(message = "Sale start must be in the future")
    private LocalDateTime saleStart;
    @NotNull(message = "Sale end time is required")
    @Future(message = "Sale end must be in the future")
    private LocalDateTime saleEnd;
    @Min(value = 1, message = "Total tickets must be at least 1")
    private int totalTickets;
}

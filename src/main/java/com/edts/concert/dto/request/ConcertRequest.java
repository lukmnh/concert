package com.edts.concert.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertRequest {
    @NotBlank(message = "Concert name is required")
    private String name;
    @NotBlank(message = "Venue is required")
    private String venue;
    private String description;
    @Valid
    @NotEmpty(message = "At least one ticket slot is required")
    private List<TicketSlotRequest> ticketSlots;
}

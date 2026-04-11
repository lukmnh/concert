package com.edts.concert.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

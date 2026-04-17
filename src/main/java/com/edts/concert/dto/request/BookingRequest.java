package com.edts.concert.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    @NotNull(message = "Slot ID is required")
    private Long slotId;
    @NotNull(message = "User ID is required")
    private Long userId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}

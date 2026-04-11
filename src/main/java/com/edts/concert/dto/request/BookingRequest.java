package com.edts.concert.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

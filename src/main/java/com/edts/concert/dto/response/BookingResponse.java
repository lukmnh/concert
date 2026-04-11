package com.edts.concert.dto.response;

import com.edts.concert.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private Long slotId;
    private String concertName;
    private String venue;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    private int quantity;
    private LocalDateTime bookedAt;

}

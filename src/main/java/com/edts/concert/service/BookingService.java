package com.edts.concert.service;

import com.edts.concert.dto.request.BookingRequest;
import com.edts.concert.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse bookTicket(BookingRequest request);
    List<BookingResponse> getBookingsByUser(Long userId);
}

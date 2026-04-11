package com.edts.concert.service.impl;

import com.edts.concert.dto.request.BookingRequest;
import com.edts.concert.dto.response.BookingResponse;
import com.edts.concert.entity.Booking;
import com.edts.concert.entity.Concert;
import com.edts.concert.entity.TicketSlot;
import com.edts.concert.entity.User;
import com.edts.concert.exception.BookingNotAllowedException;
import com.edts.concert.exception.DuplicateBookingException;
import com.edts.concert.exception.ResourceNotFoundException;
import com.edts.concert.repository.BookingRepository;
import com.edts.concert.repository.TicketSlotRepository;
import com.edts.concert.repository.UserRepository;
import com.edts.concert.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final TicketSlotRepository slotRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponse bookTicket(BookingRequest request) {

        User user = findUserOrThrow(request.getUserId());
        TicketSlot slot = findSlotOrThrow(request.getSlotId());

        validateBookingWindow(slot);
        validateNoDuplicateBooking(slot, user);

        slot.decreaseRemainingTickets(request.getQuantity());
        slotRepository.save(slot);

        Booking booking = Booking.builder()
                .slot(slot)
                .user(user)
                .quantity(request.getQuantity())
                .bookedAt(LocalDateTime.now())
                .build();

        bookingRepository.save(booking);

        return setTheResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::setTheResponse)
                .collect(Collectors.toList());
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private TicketSlot findSlotOrThrow(Long slotId) {
        return slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket slot not found with id: " + slotId));
    }

    private void validateBookingWindow(TicketSlot slot) {
        if (!slot.isBookingWindowOpen()) {
            throw new BookingNotAllowedException(
                    "Booking window is not open. Sale period: "
                            + slot.getSaleStart() + " to " + slot.getSaleEnd()
            );
        }
    }

    private void validateNoDuplicateBooking(TicketSlot slot, User user) {
        if (bookingRepository.existsBySlotIdAndUserId(slot.getId(), user.getId())) {
            throw new DuplicateBookingException("You already have a booking for this slot");
        }
    }

    private BookingResponse setTheResponse(Booking booking) {
        TicketSlot slot = booking.getSlot();
        Concert concert = slot.getConcert();

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .slotId(slot.getId())
                .concertName(concert.getName())
                .venue(concert.getVenue())
                .saleStart(slot.getSaleStart())
                .saleEnd(slot.getSaleEnd())
                .quantity(booking.getQuantity())
                .bookedAt(booking.getBookedAt())
                .build();
    }
}

package com.edts.concert.service;

import com.edts.concert.dto.request.BookingRequest;
import com.edts.concert.dto.response.BookingResponse;
import com.edts.concert.entity.Booking;
import com.edts.concert.entity.Concert;
import com.edts.concert.entity.TicketSlot;
import com.edts.concert.entity.User;
import com.edts.concert.exception.BusinessException;
import com.edts.concert.exception.ErrorCode;
import com.edts.concert.repository.BookingRepository;
import com.edts.concert.repository.TicketSlotRepository;
import com.edts.concert.repository.UserRepository;
import com.edts.concert.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private TicketSlotRepository slotRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void bookTicket_shouldSucceed_whenWindowIsOpenAndTicketsAvailable() {
        User user          = buildUser(1L);
        TicketSlot slot    = buildOpenSlot(1L, 100);
        BookingRequest req = buildRequest(1L, 1L, 1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlotIdAndUserId(1L, 1L)).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            b.setId(99L);
            return b;
        });

        BookingResponse response = bookingService.bookTicket(req);

        assertThat(response).isNotNull();
        assertThat(slot.getRemainingTickets()).isEqualTo(99);
        verify(slotRepository).save(slot);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void bookTicket_shouldThrow_whenBookingWindowHasNotStarted() {
        User user      = buildUser(1L);
        TicketSlot slot = buildFutureSlot(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));

        assertThatThrownBy(() -> bookingService.bookTicket(buildRequest(1L, 1L, 1)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(ErrorCode.BOOKING_WINDOW_CLOSED));
    }

    @Test
    void bookTicket_shouldThrow_whenBookingWindowHasClosed() {
        User user       = buildUser(1L);
        TicketSlot slot = buildClosedSlot(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));

        assertThatThrownBy(() -> bookingService.bookTicket(buildRequest(1L, 1L, 1)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(ErrorCode.BOOKING_WINDOW_CLOSED));
    }

    @Test
    void bookTicket_shouldThrow_whenUserAlreadyBookedThisSlot() {
        User user       = buildUser(1L);
        TicketSlot slot = buildOpenSlot(1L, 100);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlotIdAndUserId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> bookingService.bookTicket(buildRequest(1L, 1L, 1)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(ErrorCode.DUPLICATE_BOOKING));
    }

    @Test
    void bookTicket_shouldThrow_whenNoTicketsRemaining() {
        User user       = buildUser(1L);
        TicketSlot slot = buildOpenSlot(1L, 0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlotIdAndUserId(1L, 1L)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.bookTicket(buildRequest(1L, 1L, 1)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(ErrorCode.INSUFFICIENT_TICKETS));
    }

    @Test
    void bookTicket_shouldThrow_whenRequestedQuantityExceedsRemaining() {
        User user       = buildUser(1L);
        TicketSlot slot = buildOpenSlot(1L, 2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlotIdAndUserId(1L, 1L)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.bookTicket(buildRequest(1L, 1L, 5)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(ErrorCode.INSUFFICIENT_TICKETS));
    }

    @Test
    void bookTicket_shouldThrow_whenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.bookTicket(buildRequest(99L, 1L, 1)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(ErrorCode.USER_NOT_FOUND));
    }

    @Test
    void bookTicket_shouldThrow_whenSlotDoesNotExist() {
        User user = buildUser(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.bookTicket(buildRequest(1L, 99L, 1)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(ErrorCode.TICKET_SLOT_NOT_FOUND));
    }

    private User buildUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("Test User");
        user.setEmail("test@example.com");
        return user;
    }

    private TicketSlot buildOpenSlot(Long id, int remaining) {
        TicketSlot slot = new TicketSlot();
        slot.setId(id);
        slot.setConcert(buildConcert());
        slot.setSaleStart(LocalDateTime.now().minusMinutes(5));
        slot.setSaleEnd(LocalDateTime.now().plusMinutes(15));
        slot.setTotalTickets(10000);
        slot.setRemainingTickets(remaining);
        return slot;
    }

    private TicketSlot buildFutureSlot(Long id) {
        TicketSlot slot = new TicketSlot();
        slot.setId(id);
        slot.setConcert(buildConcert());
        slot.setSaleStart(LocalDateTime.now().plusHours(1));
        slot.setSaleEnd(LocalDateTime.now().plusHours(2));
        slot.setTotalTickets(10000);
        slot.setRemainingTickets(10000);
        return slot;
    }

    private TicketSlot buildClosedSlot(Long id) {
        TicketSlot slot = new TicketSlot();
        slot.setId(id);
        slot.setConcert(buildConcert());
        slot.setSaleStart(LocalDateTime.now().minusHours(2));
        slot.setSaleEnd(LocalDateTime.now().minusHours(1));
        slot.setTotalTickets(10000);
        slot.setRemainingTickets(5000);
        return slot;
    }

    private Concert buildConcert() {
        Concert concert = new Concert();
        concert.setId(1L);
        concert.setName("Test Concert");
        concert.setVenue("Test Venue");
        return concert;
    }

    private BookingRequest buildRequest(Long userId, Long slotId, int quantity) {
        BookingRequest req = new BookingRequest();
        req.setUserId(userId);
        req.setSlotId(slotId);
        req.setQuantity(quantity);
        return req;
    }
}
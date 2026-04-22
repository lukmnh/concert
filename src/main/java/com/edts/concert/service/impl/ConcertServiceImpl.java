package com.edts.concert.service.impl;

import com.edts.concert.dto.request.ConcertRequest;
import com.edts.concert.dto.request.TicketSlotRequest;
import com.edts.concert.dto.response.ConcertResponse;
import com.edts.concert.dto.response.TicketSlotResponse;
import com.edts.concert.entity.Concert;
import com.edts.concert.entity.TicketSlot;
import com.edts.concert.exception.BusinessException;
import com.edts.concert.exception.ErrorCode;
import com.edts.concert.repository.ConcertRepository;
import com.edts.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ConcertServiceImpl implements ConcertService {
    private final ConcertRepository concertRepository;

    @Override
    public ConcertResponse createConcert(ConcertRequest request) {
        Concert concert = buildConcertFromRequest(request);
        Concert saved = concertRepository.save(concert);
        return setTheResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ConcertResponse getConcertById(Long id) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONCERT_NOT_FOUND, "id: " + id));
        return setTheResponse(concert);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcertResponse> searchConcerts(String name, String venue) {
        List<Concert> results = findConcertsByFilters(name, venue);
        return results.stream()
                .map(this::setTheResponse)
                .collect(Collectors.toList());
    }

    private Concert buildConcertFromRequest(ConcertRequest request) {
        Concert concert = new Concert();
        concert.setName(request.getName());
        concert.setVenue(request.getVenue());
        concert.setDescription(request.getDescription());
        concert.setCreatedAt(LocalDateTime.now());

        List<TicketSlot> slots = request.getTicketSlots()
                .stream()
                .map(slotRequest -> buildTicketSlotFromRequest(slotRequest, concert))
                .collect(Collectors.toList());

        concert.setTicketSlots(slots);
        return concert;
    }

    private TicketSlot buildTicketSlotFromRequest(TicketSlotRequest request, Concert concert) {
        if (!request.getSaleEnd().isAfter(request.getSaleStart())) {
            throw new BusinessException(
                    ErrorCode.INVALID_SLOT_DATE_RANGE,
                    "saleStart: " + request.getSaleStart() + ", saleEnd: " + request.getSaleEnd()
            );
        }
        TicketSlot slot = new TicketSlot();
        slot.setConcert(concert);
        slot.setSaleStart(request.getSaleStart());
        slot.setSaleEnd(request.getSaleEnd());
        slot.setTotalTickets(request.getTotalTickets());
        slot.setRemainingTickets(request.getTotalTickets());
        return slot;
    }

    private ConcertResponse setTheResponse(Concert concert) {
        if (concert == null) return null;

        return ConcertResponse.builder()
                .id(concert.getId())
                .name(concert.getName())
                .venue(concert.getVenue())
                .description(concert.getDescription())
                .createdAt(concert.getCreatedAt())
                .ticketSlots(buildTicketSlotResponseList(concert.getTicketSlots()))
                .build();
    }

    private List<TicketSlotResponse> buildTicketSlotResponseList(List<TicketSlot> slots) {
        if (slots == null) return null;
        return slots.stream()
                .map(this::buildTicketSlotResponse)
                .collect(Collectors.toList());
    }

    private TicketSlotResponse buildTicketSlotResponse(TicketSlot slot) {
        if (slot == null) return null;
        return TicketSlotResponse.builder()
                .id(slot.getId())
                .saleStart(slot.getSaleStart())
                .saleEnd(slot.getSaleEnd())
                .totalTickets(slot.getTotalTickets())
                .remainingTickets(slot.getRemainingTickets())
                .build();
    }

    private List<Concert> findConcertsByFilters(String name, String venue) {
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasVenue = venue != null && !venue.trim().isEmpty();

        if (hasName && hasVenue) {
            return concertRepository.findByNameContainingIgnoreCaseAndVenueContainingIgnoreCase(name, venue);
        }
        if (hasName) {
            return concertRepository.findByNameContainingIgnoreCase(name);
        }
        if (hasVenue) {
            return concertRepository.findByVenueContainingIgnoreCase(venue);
        }
        return concertRepository.findAll();
    }
}

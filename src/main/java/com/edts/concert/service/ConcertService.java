package com.edts.concert.service;

import com.edts.concert.dto.request.ConcertRequest;
import com.edts.concert.dto.response.ConcertResponse;

import java.util.List;

public interface ConcertService {
    ConcertResponse createConcert(ConcertRequest request);
    ConcertResponse getConcertById(Long id);
    List<ConcertResponse> searchConcerts(String name, String venue);
}

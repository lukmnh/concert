package com.edts.concert.controller;

import com.edts.concert.dto.request.ConcertRequest;
import com.edts.concert.dto.response.ConcertResponse;
import com.edts.concert.service.ConcertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/api/concert")
@RequiredArgsConstructor
public class ConcertController {
    private final ConcertService concertService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConcertResponse> createConcert(@Valid @RequestBody ConcertRequest request) {
        ConcertResponse response = concertService.createConcert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ConcertResponse> getConcert(@PathVariable Long id) {
        return ResponseEntity.ok(concertService.getConcertById(id));
    }


    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ConcertResponse>> searchConcerts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String venue) {
        return ResponseEntity.ok(concertService.searchConcerts(name, venue));
    }
}

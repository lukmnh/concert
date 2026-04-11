package com.edts.concert.repository;

import com.edts.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    List<Concert> findByNameContainingIgnoreCase(String name);
    List<Concert> findByVenueContainingIgnoreCase(String venue);
    List<Concert> findByNameContainingIgnoreCaseAndVenueContainingIgnoreCase(String name, String venue);
}

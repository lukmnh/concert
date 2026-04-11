package com.edts.concert.repository;

import com.edts.concert.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsBySlotIdAndUserId(Long slotId, Long userId);
    List<Booking> findByUserId(Long userId);
}

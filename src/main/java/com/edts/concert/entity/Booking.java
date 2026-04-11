package com.edts.concert.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "bookings",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_booking_slot_user", columnNames = {"slot_id", "user_id"})
        }
)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private TicketSlots slot;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private int quantity;
    @Column(name = "booked_at", nullable = false)
    private LocalDateTime bookedAt;

    @PrePersist
    public void prePersist() {
        this.bookedAt = LocalDateTime.now();
    }

}

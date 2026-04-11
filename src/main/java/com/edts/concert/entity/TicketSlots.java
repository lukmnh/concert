package com.edts.concert.entity;

import com.edts.concert.exception.SoldOutException;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_slots")
public class TicketSlots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;
    @Column(name = "sale_start", nullable = false)
    private LocalDateTime saleStart;
    @Column(name = "sale_end", nullable = false)
    private LocalDateTime saleEnd;
    @Column(name = "total_tickets", nullable = false)
    private int totalTickets;
    @Column(name = "remaining_tickets", nullable = false)
    private int remainingTickets;
    @Version
    private Long version;


    public boolean isBookingWindowOpen() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(saleStart) && now.isBefore(saleEnd);
    }

    public void decreaseRemainingTickets(int quantity) {
        if (this.remainingTickets < quantity) {
            throw new SoldOutException("Not enough tickets. Remaining: " + this.remainingTickets);
        }
        this.remainingTickets -= quantity;
    }
}

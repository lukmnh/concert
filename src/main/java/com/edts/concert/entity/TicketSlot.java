package com.edts.concert.entity;

import com.edts.concert.exception.BusinessException;
import com.edts.concert.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_slots")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TicketSlot {

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
}
package com.edts.concert.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(
            "ERR-001",
            "User not found",
            HttpStatus.NOT_FOUND,
            ErrorType.RESOURCE_NOT_FOUND
    ),
    CONCERT_NOT_FOUND(
            "ERR-002",
            "Concert not found",
            HttpStatus.NOT_FOUND,
            ErrorType.RESOURCE_NOT_FOUND
    ),
    TICKET_SLOT_NOT_FOUND(
            "ERR-003",
            "Ticket slot not found",
            HttpStatus.NOT_FOUND,
            ErrorType.RESOURCE_NOT_FOUND
    ),

    // 409 - Conflict
    DUPLICATE_BOOKING(
            "ERR-004",
            "You already have a booking for this slot",
            HttpStatus.CONFLICT,
            ErrorType.CONFLICT
    ),
    OPTIMISTIC_LOCK_CONFLICT(
            "ERR-005",
            "Request conflict due to concurrent update, please try again",
            HttpStatus.CONFLICT,
            ErrorType.CONFLICT
    ),
    DUPLICATE_EMAIL(
            "ERR-006",
            "Email address is already registered",
            HttpStatus.CONFLICT,
            ErrorType.CONFLICT
    ),

    // 422 - Unprocessable Entity
    BOOKING_WINDOW_CLOSED(
            "ERR-007",
            "Booking window is not open for this slot",
            HttpStatus.UNPROCESSABLE_ENTITY,
            ErrorType.BUSINESS_RULE_VIOLATION
    ),
    INSUFFICIENT_TICKETS(
            "ERR-008",
            "Requested quantity exceeds available tickets",
            HttpStatus.UNPROCESSABLE_ENTITY,
            ErrorType.BUSINESS_RULE_VIOLATION
    ),
    INVALID_SLOT_DATE_RANGE(
            "ERR-009",
            "Sale end time must be after sale start time",
            HttpStatus.UNPROCESSABLE_ENTITY,
            ErrorType.BUSINESS_RULE_VIOLATION
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    private final ErrorType type;

    public enum ErrorType {
        RESOURCE_NOT_FOUND,
        CONFLICT,
        BUSINESS_RULE_VIOLATION
    }
}

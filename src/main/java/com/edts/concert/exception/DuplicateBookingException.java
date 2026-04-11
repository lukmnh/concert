package com.edts.concert.exception;

public class DuplicateBookingException extends RuntimeException {
    public DuplicateBookingException(String message) { super(message); }
}

package com.edts.concert.exception;

public class BookingNotAllowedException extends RuntimeException {
    public BookingNotAllowedException(String message) { super(message); }
}

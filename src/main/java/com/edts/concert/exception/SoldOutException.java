package com.edts.concert.exception;

public class SoldOutException extends RuntimeException {
    public SoldOutException(String message) { super(message); }
}

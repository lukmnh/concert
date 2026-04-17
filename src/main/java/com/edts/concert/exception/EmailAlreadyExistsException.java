package com.edts.concert.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public  EmailAlreadyExistsException(String message){
        super(message);
    }
}

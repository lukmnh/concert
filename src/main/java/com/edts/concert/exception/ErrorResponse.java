package com.edts.concert.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final String code;
    private final String type;
    private final String message;
    private final String detail;
    private final LocalDateTime timestamp;

    public ErrorResponse(String code, String type, String message, String detail) {
        this.code      = code;
        this.type      = type;
        this.message   = message;
        this.detail    = detail;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getType().name(),
                errorCode.getMessage(),
                null
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, String detail) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getType().name(),
                errorCode.getMessage(),
                detail
        );
    }

    public static ErrorResponse validation(String message) {
        return new ErrorResponse(
                "ERR-400",
                "VALIDATION_ERROR",
                message,
                null
        );
    }
}

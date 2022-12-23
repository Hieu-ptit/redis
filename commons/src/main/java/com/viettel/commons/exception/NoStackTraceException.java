package com.viettel.commons.exception;

public class NoStackTraceException extends RuntimeException {
    public NoStackTraceException(String message) {
        super(message, null, false, false);
    }
}

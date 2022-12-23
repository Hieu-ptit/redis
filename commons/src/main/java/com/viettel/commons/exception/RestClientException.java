package com.viettel.commons.exception;


public class RestClientException extends NoStackTraceException {
    private final int httpStatus;
    private final byte[] body;
    private String message;

    public RestClientException(int httpStatus, byte[] body) {
        super(null);
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String getMessage() {
        if (message == null) {
            message =  httpStatus + " " + body;
        }
        return message;
    }
}

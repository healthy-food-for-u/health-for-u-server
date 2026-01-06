package com.healthforu.common.exception;

public abstract class BusinessException extends RuntimeException {
    private final int statusCode;

    public BusinessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

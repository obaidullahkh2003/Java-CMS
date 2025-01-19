package com.obaidullah.cms.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedAPIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public AccessDeniedAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public AccessDeniedAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

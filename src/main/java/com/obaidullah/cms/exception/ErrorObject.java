package com.obaidullah.cms.exception;

import java.util.Date;

public class ErrorObject {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorObject(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

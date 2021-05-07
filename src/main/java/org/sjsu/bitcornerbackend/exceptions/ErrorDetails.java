package org.sjsu.bitcornerbackend.exceptions;

import java.util.Date;
import java.util.List;

public class ErrorDetails {
    private Date timestamp;
    private List<String> message;
    private String details;

    public ErrorDetails(Date timestamp, List<String> message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<String> getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}

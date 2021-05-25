package org.sjsu.bitcornerbackend.exceptions.billExceptions;

public class SamePayerSenderException extends Exception {
    private static final long serialVersionUID = 1L;

    public SamePayerSenderException(String message) {
        super(message);
    }
}

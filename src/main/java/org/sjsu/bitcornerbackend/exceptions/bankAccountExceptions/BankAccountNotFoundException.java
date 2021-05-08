package org.sjsu.bitcornerbackend.exceptions.bankAccountExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BankAccountNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public BankAccountNotFoundException(String message) {
        super(message);
    }
}

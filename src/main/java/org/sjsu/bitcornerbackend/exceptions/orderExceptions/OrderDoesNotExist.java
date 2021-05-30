package org.sjsu.bitcornerbackend.exceptions.orderExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderDoesNotExist extends Exception {
    private static final long serialVersionUID = 1L;

    public OrderDoesNotExist(String message) {
        super(message);
    }

}

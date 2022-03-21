package com.boot.webjava.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends IllegalArgumentException {
    public CustomerNotFoundException() {
        super("Customer Not Found!");
    }
}

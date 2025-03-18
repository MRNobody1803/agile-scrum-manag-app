package com.example.demo_sprinboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductBacklogNotFoundException extends RuntimeException {
    public ProductBacklogNotFoundException(String message) {
        super(message);
    }
}


package com.project.petshop.petshop.exceptions.address;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AddressAlreadyExist extends RuntimeException {
    public AddressAlreadyExist(String message) {
        super(message);
    }
}

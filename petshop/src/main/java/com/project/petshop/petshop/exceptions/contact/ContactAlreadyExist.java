package com.project.petshop.petshop.exceptions.contact;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ContactAlreadyExist extends RuntimeException {
    public ContactAlreadyExist(String message) {
        super(message);
    }
}

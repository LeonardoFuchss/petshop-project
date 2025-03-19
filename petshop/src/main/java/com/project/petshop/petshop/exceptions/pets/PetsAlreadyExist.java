package com.project.petshop.petshop.exceptions.pets;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PetsAlreadyExist extends RuntimeException {
    public PetsAlreadyExist(String message) {
        super(message);
    }
}

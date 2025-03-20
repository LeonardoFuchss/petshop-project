package com.project.petshop.petshop.exceptions.breed;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BreedAlreadyExist extends RuntimeException {
    public BreedAlreadyExist(String message) {
        super(message);
    }
}

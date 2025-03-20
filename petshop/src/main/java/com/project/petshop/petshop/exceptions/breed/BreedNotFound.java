package com.project.petshop.petshop.exceptions.breed;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BreedNotFound extends RuntimeException {
    public BreedNotFound(String message) {
        super(message);
    }
}

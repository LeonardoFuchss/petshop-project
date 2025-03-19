package com.project.petshop.petshop.exceptions.pets;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PetsNotFound extends RuntimeException {
    public PetsNotFound(String message) {
        super(message);
    }
}

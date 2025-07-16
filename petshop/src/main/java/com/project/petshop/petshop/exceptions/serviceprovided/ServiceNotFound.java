package com.project.petshop.petshop.exceptions.serviceprovided;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ServiceNotFound extends RuntimeException {
    public ServiceNotFound(String message) {
        super(message);
    }
}

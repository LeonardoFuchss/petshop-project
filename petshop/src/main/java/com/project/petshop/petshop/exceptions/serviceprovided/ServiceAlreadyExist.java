package com.project.petshop.petshop.exceptions.serviceprovided;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ServiceAlreadyExist extends RuntimeException {
    public ServiceAlreadyExist(String message) {
        super(message);
    }
}

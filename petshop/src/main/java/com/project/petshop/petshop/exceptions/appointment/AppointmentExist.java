package com.project.petshop.petshop.exceptions.appointment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AppointmentExist extends RuntimeException {
    public AppointmentExist(String message) {
        super(message);
    }
}

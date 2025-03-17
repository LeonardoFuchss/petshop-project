package com.project.petshop.petshop.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    @NotNull(message = "The pet cannot be null and void.")
    private Long idPet;
    @NotNull(message = "Description cannot be null")
    @Size(min = 5, max = 70, message = "Invalid description size")
    private String description;
    @Positive(message = "Price must be positive. (>0)")
    private Double price;
    @Future(message = "Appointment date must be in the future.")
    private Date date;
}

package com.project.petshop.petshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.petshop.petshop.model.entities.Pet;
import com.project.petshop.petshop.model.entities.ServiceProvided;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    @NotNull(message = "The pet cannot be null or void.")
    private Long petId;
    private Long serviceProvidedId;
    @Future(message = "Appointment date must be in the future.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
}

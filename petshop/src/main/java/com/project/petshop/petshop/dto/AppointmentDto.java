package com.project.petshop.petshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    @NotNull(message = "The pet cannot be null or void.")
    private Long idPet;
    @NotNull(message = "Description cannot be null")
    @Size(min = 5, max = 70, message = "Invalid description size")
    private String description;
    @Positive(message = "Price must be positive. (>0)")
    @NotNull(message = "Price cannot be null")
    private Double price;
    @Future(message = "Appointment date must be in the future.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
}

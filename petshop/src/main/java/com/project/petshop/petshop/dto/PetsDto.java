package com.project.petshop.petshop.dto;

import com.project.petshop.petshop.model.entities.Breed;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetsDto {
    @NotNull(message = "The customer cannot be null or void.")
    private Long idClient;
    @NotNull(message = "The breed value cannot be null or void.")
    private Long idBreed;
    @Past(message = "The birthday date must be in the past.")
    @NotNull(message = "Birth date cannot be null")
    private LocalDate birthDate;
    @NotNull(message = "the name cannot be null")
    @Size(min = 4, max = 30, message = "Invalid name size")
    private String dogName;
}

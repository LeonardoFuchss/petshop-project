package com.project.petshop.petshop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BreedDto {
    @NotNull(message = "Description cannot be null")
    @Size(min = 5, max = 30, message = "Invalid description size")
    private String description;
}

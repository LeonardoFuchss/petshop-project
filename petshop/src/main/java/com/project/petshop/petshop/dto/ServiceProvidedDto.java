package com.project.petshop.petshop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProvidedDto {
    @NotNull(message = "Service name should not be null.")
    private String serviceName;
    @NotNull(message = "Description should not be null.")
    private String description;
    @NotNull(message = "Price should not be null.")
    @Positive(message = "The price should be greater than zero.")
    private Double price;
}

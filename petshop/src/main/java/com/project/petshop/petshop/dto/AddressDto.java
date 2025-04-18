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
public class AddressDto {
    @NotNull(message = "The client cannot be null or void.")
    private Long idUser;
    @NotNull(message = "Street cannot be null")
    @Size(min = 3, max = 40, message = "Street name is invalid. Must be greater than 3 and less than 40 characters")
    private String street; /* Logradouro */
    @NotNull(message = "City cannot be null")
    private String city;
    private String complement;
}

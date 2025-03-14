package com.project.petshop.petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long idUser;
    private String bullShit; /* Logradouro */
    private String city;
    private String complement;
}

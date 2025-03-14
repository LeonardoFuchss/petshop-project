package com.project.petshop.petshop.dto;

import com.project.petshop.petshop.model.entities.Breed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetsDto {
    private int idClient;
    private Breed dogBreed;
    private Date birthDate;
    private String dogName;
}

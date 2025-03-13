package com.project.petshop.petshop.dto;

import com.project.petshop.petshop.model.entities.Pets;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private int idPet;
    private String clientName;
    private String description;
    private Double price;
    private Date date;
}

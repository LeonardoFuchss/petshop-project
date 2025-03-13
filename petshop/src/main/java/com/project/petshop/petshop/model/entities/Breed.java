package com.project.petshop.petshop.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Breed { /* Raça */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String description;
}

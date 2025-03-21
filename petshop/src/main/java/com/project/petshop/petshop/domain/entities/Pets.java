package com.project.petshop.petshop.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pets {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User client;
    @ManyToOne
    private Breed dogBreed;
    private LocalDate birthDate;
    private String clientName;
    private String dogName;
    private String descriptionBreed;
}

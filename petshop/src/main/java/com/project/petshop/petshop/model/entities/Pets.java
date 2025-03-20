package com.project.petshop.petshop.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

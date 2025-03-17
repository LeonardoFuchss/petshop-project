package com.project.petshop.petshop.model.entities;

import jakarta.persistence.*;
import lombok.*;

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
    private Date birthDate;
    private String clientName;
    private String dogName;
    private String descriptionBreed;
}

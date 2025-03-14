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
    private int id;
    @ManyToOne
    private User client;
    @OneToOne
    private Breed dogBreed;
    private Date birthDate;
    private String dogName;
}

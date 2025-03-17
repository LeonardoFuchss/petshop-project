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
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    private Pets pet;
    private String clientName;
    private String petName;
    private String description;
    private Double price;
    private Date date;
}

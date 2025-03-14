package com.project.petshop.petshop.model.entities;

import com.project.petshop.petshop.model.TagContact;
import com.project.petshop.petshop.model.TypeContact;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User client;
    private TagContact tag;
    private TypeContact typeContact;
    private String value;
}

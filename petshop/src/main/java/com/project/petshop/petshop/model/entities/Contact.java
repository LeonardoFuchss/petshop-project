package com.project.petshop.petshop.model.entities;

import com.project.petshop.petshop.model.enums.TagContact;
import com.project.petshop.petshop.model.enums.TypeContact;
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
    @Enumerated(EnumType.STRING)
    private TagContact tag;
    @Enumerated(EnumType.STRING)
    private TypeContact typeContact;
    private String value;
}

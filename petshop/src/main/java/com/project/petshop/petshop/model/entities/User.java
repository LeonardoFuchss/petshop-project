package com.project.petshop.petshop.model.entities;

import com.project.petshop.petshop.model.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_table")
public class User {
    @Id
    private int CPF;
    private Profile profile;
    private String fullName;
    private String password;
}

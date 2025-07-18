package com.project.petshop.petshop.model.entities;

import com.project.petshop.petshop.model.entities.enums.Profile;
import com.project.petshop.petshop.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "pessoa")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userCpf;
    @Enumerated(EnumType.STRING)
    private Profile profile;
    private String fullName;
    private String password;
    private LocalDateTime signUpDate;
    private String email;
    private String numberContact;


}

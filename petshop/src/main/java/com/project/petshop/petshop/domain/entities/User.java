package com.project.petshop.petshop.domain.entities;

import com.project.petshop.petshop.domain.enums.Profile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userCpf;
    @Enumerated(EnumType.STRING)
    private Profile profile;
    private String fullName;
    private String password;
    @NotNull(message = "Date value cannot be null")
    @PastOrPresent(message = "Invalid date. The date cannot be in the future.")
    private LocalDateTime signUpDate;
}

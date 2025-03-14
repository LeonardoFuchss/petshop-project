package com.project.petshop.petshop.dto;

import com.project.petshop.petshop.model.Profile;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userCpf;
    @Enumerated(EnumType.STRING)
    private Profile profile;
    private String fullName;
    private String password;
    private Date signUpDate;
}

package com.project.petshop.petshop.dto;

import com.project.petshop.petshop.model.Profile;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Profile profile;
    private String fullName;
    private String password;
}

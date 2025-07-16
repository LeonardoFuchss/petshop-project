package com.project.petshop.petshop.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull(message = "CPF cannot be null")
    private String userCpf;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Profile cannot be null")
    @Pattern(regexp = "ADMIN|CLIENT", message = "Invalid Profile. Use 'ADMIN' or 'CLIENT' to define the user type.")
    private String profile;
    @NotNull(message = "Name cannot be null")
    @Size(min = 4, max = 30, message = "Invalid name size. Use at least 4 characters in your name.")
    private String fullName;
    @NotBlank(message = "Password cannot be null")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must have at least one uppercase letter, a lowercase, a number and a special character"
    )
    private String password;
    private String email;
    private String number;
}

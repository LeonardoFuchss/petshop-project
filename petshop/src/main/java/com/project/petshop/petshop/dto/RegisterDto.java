package com.project.petshop.petshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotNull(message = "The CPF cannot be null")
    @CPF(message = "Invalid CPF")
    private String userCpf;
    @NotNull(message = "The name cannot be null")
    @Size(min = 4, max = 30, message = "Invalid name size. Use at least 4 characters in your name.")
    private String fullName;
    @NotBlank(message = "The password cannot be null")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "The password must have at least one uppercase letter, a lowercase, a number and a special character"
    )
    private String password;
}

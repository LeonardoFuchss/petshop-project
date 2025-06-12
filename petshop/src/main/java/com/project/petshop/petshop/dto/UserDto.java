package com.project.petshop.petshop.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull(message = "The CPF value cannot be null")
    @CPF
    private String userCpf;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "The profile value cannot be null")
    @Pattern(regexp = "ADMIN|CLIENT", message = "Inválid Profile. Use ADMIN or CLIENT")
    private String profile;
    @NotNull(message = "Name cannot be null")
    @Size(min = 4, max = 30, message = "Invalid full name size")
    private String fullName;
    @NotBlank(message = "The password value cannot be null")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "The password must have at least one uppercase letter, a lowercase, a number and a special character"
    )
    private String password;
}

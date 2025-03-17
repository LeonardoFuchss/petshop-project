package com.project.petshop.petshop.dto;

import com.project.petshop.petshop.model.enums.Profile;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull(message = "The CPF value cannot be null")
    @CPF(message = "Invalid CPF")
    private String userCpf;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "The profile value cannot be null")
    @Pattern(regexp = "ADMIN|CLIENT", message = "Inválid Profile. Use ADMIN or CLIENT")
    private Profile profile;
    @NotNull(message = "the name cannot be null")
    @Size(min = 4, max = 30, message = "Invalid name size")
    private String fullName;
    @NotNull(message = "The password value cannot be null")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "The password must have at least one uppercase letter, a lowercase, a number and a special character"
    )
    private String password;
    @NotNull(message = "Date value cannot be null")
    @PastOrPresent(message = "Invalid date. The date cannot be in the future.")
    private Date signUpDate;
}

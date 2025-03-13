package com.project.petshop.petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private String fullName;
    private int idUser;
    private Date signUpDate;
}

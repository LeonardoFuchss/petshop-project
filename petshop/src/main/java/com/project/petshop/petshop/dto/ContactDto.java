package com.project.petshop.petshop.dto;

import com.project.petshop.petshop.model.TagContact;
import com.project.petshop.petshop.model.TypeContact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactDto {
    private Long idUser;
    private TagContact tag;
    private TypeContact typeContact;
    private String value;
}

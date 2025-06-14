package com.project.petshop.petshop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactDto {
    @NotNull(message = "The customer cannot be null or void.")
    private Long idUser;
    @Pattern(regexp = "PERSONAL|WORK|EMERGENCY|WHATSAPP|ANOTHER", message = "Inválid TAG. Use PERSONAL, WORK, EMERGENCY or WHATSAPP")
    private String tag;
    @Pattern(regexp = "EMAIL|PHONE", message = "Invalid type contact. Use EMAIL or PHONE.")
    @NotNull(message = "Type Contact cannot be null.")
    private String contactType;
    @NotNull(message = "Phone or Email cannot be null.")
    private String value;
    /* validar value para contato válido */
}

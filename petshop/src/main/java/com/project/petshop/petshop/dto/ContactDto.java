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
    @NotNull(message = "User cannot be null.")
    private Long idUser;
    @Pattern(regexp = "PERSONAL|WORK|EMERGENCY|WHATSAPP|ANOTHER", message = "Invalid TAG. Use tags like this: PERSONAL, WORK, EMERGENCY or WHATSAPP")
    private String tag;
    @Pattern(regexp = "EMAIL|PHONE", message = "Invalid contact type. Use EMAIL or PHONE.")
    @NotNull(message = "Contact type cannot be null.")
    private String contactType;
    @NotNull(message = "Phone or Email cannot be null.")
    private String value;
    /* validar value para contato válido */
}

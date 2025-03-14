package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.model.entities.Contact;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    @Autowired
    private UserRepository userRepository;

    public Contact toEntity(ContactDto contactDto) {

        User user = userRepository.findById(contactDto.getIdUser()).orElse(null);

        return Contact.builder()
                .typeContact(contactDto.getTypeContact())
                .value(contactDto.getValue())
                .client(user)
                .build();
    }
}

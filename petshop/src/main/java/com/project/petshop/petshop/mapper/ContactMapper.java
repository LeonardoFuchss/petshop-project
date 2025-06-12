package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.domain.entities.Contact;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.domain.enums.TagContact;
import com.project.petshop.petshop.domain.enums.TypeContact;
import com.project.petshop.petshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ContactMapper {

    @Autowired
    private UserRepository userRepository;

    public Contact toEntity(ContactDto contactDto) {

        TagContact tagContact = TagContact.valueOf(contactDto.getTag());
        TypeContact typeContact = TypeContact.valueOf(contactDto.getContactType());

        Optional<User> user = userRepository.findById(contactDto.getIdUser());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        } else {
            return Contact.builder()
                    .typeContact(typeContact)
                    .tag(tagContact)
                    .value(contactDto.getValue())
                    .client(user.get())
                    .build();
        }
    }
}

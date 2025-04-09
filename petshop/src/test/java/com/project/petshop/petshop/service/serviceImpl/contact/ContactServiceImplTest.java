package com.project.petshop.petshop.service.serviceImpl.contact;

import com.project.petshop.petshop.domain.entities.Contact;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.domain.enums.Profile;
import com.project.petshop.petshop.domain.enums.TagContact;
import com.project.petshop.petshop.domain.enums.TypeContact;
import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.exceptions.contact.ContactAlreadyExist;
import com.project.petshop.petshop.mapper.ContactMapper;
import com.project.petshop.petshop.repository.ContactRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ContactMapper contactMapper;
    @InjectMocks
    private ContactServiceImpl contactService;

    @Nested
    class saveContact {
        @Test
        @DisplayName("Contact created successfully")
        void shouldSaveContactWithSuccess() {
            User user = new User(1L, "05416199008", Profile.ADMIN, "Leonardo Dos Santos Fuchs", "testeSenha10", LocalDateTime.now());
            Contact contact = new Contact(1L, user, TagContact.PERSONAL, TypeContact.EMAIL, "fuchsleonardo01@gmail.com");
            ContactDto contactDto = new ContactDto(1L, "EMAIL", "PERSONAL", "fuchsleonardo01@gmail.com");

            doReturn(contact).when(contactRepository).save(contact);
            when(contactMapper.toEntity(contactDto)).thenReturn(contact);
            when(contactRepository.findByValue(contact.getValue())).thenReturn(null);

            Contact savedContact = contactService.save(contactDto);

            assertNotNull(savedContact);
            assertEquals(contactDto.getValue(), savedContact.getValue());
        }

        @Test
        @DisplayName("Contact conflict")
        void shouldShowContactConflict() {
            User user = new User(1L, "05416199008", Profile.ADMIN, "Leonardo Dos Santos Fuchs", "testeSenha10", LocalDateTime.now());
            Contact contact = new Contact(1L, user, TagContact.PERSONAL, TypeContact.EMAIL, "fuchsleonardo01@gmail.com");
            ContactDto contactDto = new ContactDto(1L, "EMAIL", "PERSONAL", "fuchsleonardo01@gmail.com");

            when(contactMapper.toEntity(contactDto)).thenReturn(contact);
            when(contactRepository.findByValue(contact.getValue())).thenReturn(contact);

            assertThrows(ContactAlreadyExist.class, () -> contactService.save(contactDto));

        }
    }

}
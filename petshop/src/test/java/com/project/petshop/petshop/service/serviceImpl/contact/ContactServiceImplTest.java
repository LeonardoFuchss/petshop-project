package com.project.petshop.petshop.service.serviceImpl.contact;

import com.project.petshop.petshop.domain.entities.Contact;
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

import static org.junit.jupiter.api.Assertions.*;
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
            Contact contact = new Contact();
        }
    }

}
package com.project.petshop.petshop.service.interfaces.contact;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.domain.entities.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    Contact createContact(ContactDto contactDto);
    Contact findContactById(Long id);
    List<Contact> findAllContacts();
    void deleteContactById(Long id);
}

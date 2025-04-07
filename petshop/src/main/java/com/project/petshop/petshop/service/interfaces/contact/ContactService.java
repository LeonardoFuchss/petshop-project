package com.project.petshop.petshop.service.interfaces.contact;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.domain.entities.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    public Contact save(ContactDto contactDto);
    public List<Contact> findAll();
    public Optional<Contact> findById(Long id);
    public void delete(Long id);
}

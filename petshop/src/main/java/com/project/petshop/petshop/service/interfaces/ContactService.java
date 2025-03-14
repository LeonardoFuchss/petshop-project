package com.project.petshop.petshop.service.interfaces;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.model.entities.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    public void save(ContactDto contactDto);
    public List<Contact> findAll();
    public Optional<Contact> findById(Long id);
    public void delete(Long id);
}

package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.mapper.ContactMapper;
import com.project.petshop.petshop.model.entities.Contact;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.repository.ContactRepository;
import com.project.petshop.petshop.service.interfaces.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ContactMapper contactMapper;

    @Override
    public void save(ContactDto contactDto) {
        Contact contact = contactMapper.toEntity(contactDto);
        contactRepository.save(contact);
    }

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return contactRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
       Optional<Contact> contact = contactRepository.findById(id);
        contact.ifPresent(value -> contactRepository.delete(value));
    }
}

package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.exceptions.contact.ContactAlreadyExist;
import com.project.petshop.petshop.exceptions.contact.ContactNotFound;
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
    public void save(ContactDto contactDto) { /* Persiste um novo contato */
        Contact contact = contactMapper.toEntity(contactDto); /* Mapeamento de DTO para entidade */
        Contact contactFind = contactRepository.findByValue(contact.getValue()); /* Consulta que busca o contato no banco de dados */
        if (contactFind == null) { /* Verifica se o contato não existe */
            contactRepository.save(contact); /* Persiste no banco de dados */
        } else {
            throw new ContactAlreadyExist("Contact already exist");
        }

    }
    @Override
    public List<Contact> findAll() { /* Busca uma lista de contatos */
        List<Contact> contacts = contactRepository.findAll();
        if (contacts.isEmpty()) { /* Verifica se a lista está vazia */
            throw new ContactNotFound("Contacts not found");
        }
        return contactRepository.findAll();
    }

    @Override
    public Optional<Contact> findById(Long id) { /* Busca um contato específico com base no seu ID */
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isEmpty()) { /* Verifica se a consulta retornou vazia */
            throw new ContactNotFound("Contact not found");
        } else {
            return contactRepository.findById(id);
        }
    }

    @Override
    public void delete(Long id) { /* Deleta um usuário com base no seu ID */
       Optional<Contact> contact = contactRepository.findById(id); /* Busca um usuário com base no ID */
       if (contact.isPresent()) { /* Verifica se o usuário existe. */
           contactRepository.delete(contact.get()); /* Deleta o usuário do banco de dados. */
       } else {
           throw new ContactNotFound("Contact not found");
       }
    }
}

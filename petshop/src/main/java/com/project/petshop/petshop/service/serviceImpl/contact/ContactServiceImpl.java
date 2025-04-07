package com.project.petshop.petshop.service.serviceImpl.contact;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.exceptions.contact.ContactAlreadyExist;
import com.project.petshop.petshop.exceptions.contact.ContactNotFound;
import com.project.petshop.petshop.mapper.ContactMapper;
import com.project.petshop.petshop.domain.entities.Contact;
import com.project.petshop.petshop.repository.ContactRepository;
import com.project.petshop.petshop.service.interfaces.contact.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {


    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;


    @Override
    public Contact save(ContactDto contactDto) { /* Persiste um novo contato */
        Contact contact = contactMapper.toEntity(contactDto); /* Mapeamento de DTO para entidade */
        Contact contactFind = contactRepository.findByValue(contact.getValue()); /* Consulta que busca o contato no banco de dados */
        if (contactFind == null) { /* Verifica se o contato não existe */
            return contactRepository.save(contact); /* Persiste no banco de dados */
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
        return contacts;
    }


    @Override
    public Optional<Contact> findById(Long id) { /* Busca um contato específico com base no seu ID */
        return  Optional.ofNullable(contactRepository.findById(id).orElseThrow(() -> new ContactNotFound("Contact not found")));
    }


    @Override
    public void delete(Long id) { /* Deleta um usuário com base no seu ID */
       Optional<Contact> contact = Optional.ofNullable(contactRepository.findById(id).orElseThrow(() -> new ContactNotFound("Contact not found"))); /* Busca um usuário com base no ID */
       Contact contactDelete = contact.get();
       contactRepository.delete(contactDelete); /* Deleta o usuário do banco de dados. */
    }
}

package com.project.petshop.petshop.service.impl.contact;

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

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {


    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;


    @Override
    public Contact createContact(ContactDto contactDto) {
        Contact contact = contactMapper.toEntity(contactDto);
        contactIsPresentByValue(contact); /* Verifica se o contato já existe. */
        contactRepository.save(contact);
        return contact;
    }


    @Override
    public List<Contact> findAllContacts() { /* Busca uma lista de contatos */
        List<Contact> contacts = contactRepository.findAll();
        contactListIsEmpty(contacts);
        return contacts;
    }


    @Override
    public Contact findContactById(Long id) { /* Busca um contato específico com base no seu ID */
        return contactRepository.findById(id).orElseThrow(() -> new ContactNotFound("Contact not found"));
    }


    @Override
    public void deleteContactById(Long id) { /* Deleta um usuário com base no seu ID */
       Contact contact = contactRepository.findById(id).orElseThrow(() -> new ContactNotFound("Contact not found"));
       contactRepository.delete(contact); /* Deleta o usuário do banco de dados. */
    }











    // Métodos reutilizáveis

    private void contactIsPresentByValue(Contact contact) {
        Contact contactFound = contactRepository.findByValue(contact.getValue());
        if (contactFound != null) {
            throw new ContactAlreadyExist("Contact already exist.");
        }
    }
    private void contactListIsEmpty(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            throw new ContactNotFound("No contact found.");
        }
    }
}

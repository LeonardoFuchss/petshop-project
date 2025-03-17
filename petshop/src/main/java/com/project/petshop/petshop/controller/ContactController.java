package com.project.petshop.petshop.controller;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.model.entities.Contact;
import com.project.petshop.petshop.service.interfaces.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contact")
@CrossOrigin("*")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/save")
    public ResponseEntity<Void> addContact(@RequestBody ContactDto contactDto) {
        contactService.save(contactDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<Contact>> findAll() {
        return ResponseEntity.ok().body(contactService.findAll());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<Contact>> findById(@PathVariable Long id) {
        Optional<Contact> contact = contactService.findById(id);
        return ResponseEntity.ok(contact);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

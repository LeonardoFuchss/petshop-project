package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.model.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    public Contact findByValue(String value);
}

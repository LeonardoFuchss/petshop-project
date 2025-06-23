package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.domain.entities.Contact;
import com.project.petshop.petshop.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Contact findByValue(String value);
    Contact findByClient(User user);
}

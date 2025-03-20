package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.model.entities.Address;
import com.project.petshop.petshop.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    public Optional<Address> findByUser(User user);
}

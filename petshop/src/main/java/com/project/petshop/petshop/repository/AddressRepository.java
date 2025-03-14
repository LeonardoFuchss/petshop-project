package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.model.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.model.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetsRepository extends JpaRepository<Pet, Long> {
}

package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.domain.entities.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {

    Breed findByDescription(String name);
}

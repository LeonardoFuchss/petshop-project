package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.model.entities.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {

    public Breed findByDescription(String name);
}

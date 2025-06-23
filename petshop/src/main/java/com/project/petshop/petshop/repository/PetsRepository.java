package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.domain.entities.Pets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetsRepository extends JpaRepository<Pets, Long> {
    Optional<Pets> findByClientName(String clientName);
    Optional<Pets> findByDogName(String dogName);
    Optional<Pets> findByClientNameAndDogName(String clientName, String dogName);
}

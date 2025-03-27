package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.domain.entities.Pets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetsRepository extends JpaRepository<Pets, Long> {
    public Optional<Pets> findByClientName(String clientName);
    public Optional<Pets> findByDogName(String dogName);
}

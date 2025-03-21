package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.domain.entities.Pets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetsRepository extends JpaRepository<Pets, Long> {
    public List<Pets> findByClientName(String clientName);
    public List<Pets> findByDogName(String dogName);
}

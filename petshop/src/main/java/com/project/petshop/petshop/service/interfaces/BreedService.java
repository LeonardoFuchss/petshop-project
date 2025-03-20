package com.project.petshop.petshop.service.interfaces;

import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.model.entities.Breed;

import java.util.List;
import java.util.Optional;

public interface BreedService {
    public Breed save(BreedDto breedDto);
    public List<Breed> findAll();
    public Optional<Breed> findById(Long id);
    public void delete(Long id);
}

package com.project.petshop.petshop.service.interfaces;

import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.model.entities.Breed;

import java.util.List;

public interface BreedService {
    public void save(BreedDto breedDto);
    public List<Breed> findAll();
    public Breed findById(Long id);
    public void delete(Long id);
}

package com.project.petshop.petshop.service.interfaces.breeds;

import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.domain.entities.Breed;

import java.util.List;
import java.util.Optional;

public interface BreedService {
    Breed createBreed(BreedDto breedDto);
    List<Breed> findAllBreeds();
    Optional<Breed> findBreedById(Long id);
    void deleteBreedById(Long id);
}

package com.project.petshop.petshop.service.interfaces.pets;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.domain.entities.Pets;

import java.util.List;
import java.util.Optional;

public interface PetsService {
    Pets createPet(PetsDto petsDto);
    List<Pets> findAllPets();
    Pets findByClientName(String clientName);
    Pets findPetById(Long id);
    void deletePetById(Long id);
    Pets updatePet(PetsDto petsDto);
}

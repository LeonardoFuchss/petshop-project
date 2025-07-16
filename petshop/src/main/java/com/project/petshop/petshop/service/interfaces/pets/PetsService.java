package com.project.petshop.petshop.service.interfaces.pets;

import com.project.petshop.petshop.dto.PetDtoPublic;
import com.project.petshop.petshop.model.entities.Pet;
import com.project.petshop.petshop.dto.PetsDto;

import java.util.List;

public interface PetsService {
    Pet createPet(PetsDto petsDto);
    Pet createPetPublic(PetDtoPublic petDtoPublic);
    List<Pet> findAllPets();
    Pet findPetById(Long id);
    void deletePetById(Long id);
    Pet updatePet(PetsDto petsDto);
}

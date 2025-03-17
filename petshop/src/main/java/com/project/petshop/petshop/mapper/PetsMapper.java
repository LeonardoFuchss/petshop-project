package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.model.entities.Breed;
import com.project.petshop.petshop.model.entities.Pets;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.repository.BreedRepository;
import com.project.petshop.petshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PetsMapper {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BreedRepository breedRepository;

    public Pets toEntity(PetsDto petsDto) {

        User user = userRepository.findById(petsDto.getIdClient()).orElse(null);
        Breed breed = breedRepository.findById(petsDto.getIdBreed()).orElse(null);

        return Pets.builder()
                .client(user)
                .dogName(petsDto.getDogName())
                .birthDate(petsDto.getBirthDate())
                .dogBreed(breed)
                .clientName(user.getFullName())
                .descriptionBreed(breed.getDescription())
                .build();
        /* implementar breed mapper */
    }
}

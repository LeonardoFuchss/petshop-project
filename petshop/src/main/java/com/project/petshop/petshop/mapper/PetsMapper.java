package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.exceptions.breed.BreedNotFound;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.model.entities.Breed;
import com.project.petshop.petshop.model.entities.Pets;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.repository.BreedRepository;
import com.project.petshop.petshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PetsMapper {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BreedRepository breedRepository;

    public Pets toEntity(PetsDto petsDto) {

        Optional<User> user = userRepository.findById(petsDto.getIdClient());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        } else {
            Optional<Breed> breed = breedRepository.findById(petsDto.getIdBreed());
            if (breed.isEmpty()) {
                throw new BreedNotFound("Breed not found");
            } else {
                return Pets.builder()
                        .client(user.get())
                        .dogName(petsDto.getDogName())
                        .birthDate(petsDto.getBirthDate())
                        .dogBreed(breed.get())
                        .clientName(user.get().getFullName())
                        .descriptionBreed(breed.get().getDescription())
                        .build();
            }
        }
    }
}

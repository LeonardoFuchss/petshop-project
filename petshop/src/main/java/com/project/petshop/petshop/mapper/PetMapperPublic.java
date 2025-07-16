package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.PetDtoPublic;
import com.project.petshop.petshop.model.entities.Pet;
import com.project.petshop.petshop.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PetMapperPublic {
        private final UserRepository userRepository;

        public Pet toEntity(PetDtoPublic petsDto) {

            return Pet.builder()
                    .dogName(petsDto.getDogName())
                    .birthDate(petsDto.getBirthDate())
                    .breed(petsDto.getBreed())
                    .build();
        }
    }

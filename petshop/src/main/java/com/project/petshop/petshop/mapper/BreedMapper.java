package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.model.entities.Breed;
import org.springframework.stereotype.Component;

@Component
public class BreedMapper {

    public Breed toEntity(BreedDto breedDto) {

        return Breed.builder()
                .description(breedDto.getDescription())
                .build();
    }
}

package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.model.entities.Pet;
import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PetsMapper {
    private final UserRepository userRepository;

    public Pet toEntity(PetsDto petsDto) {

        Optional<User> user = Optional.ofNullable(userRepository.findById(petsDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found")));

        return Pet.builder()
                .client(user.get())
                .dogName(petsDto.getDogName())
                .birthDate(petsDto.getBirthDate())
                .breed(petsDto.getBreed())
                .build();
    }
}



package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.model.entities.Address;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressMapper {

    @Autowired
    private UserRepository userRepository;

    public Address toEntity(AddressDto addressDto) {

        Optional<User> user = userRepository.findById(addressDto.getIdUser());

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        } else {
            return Address.builder()
                    .city(addressDto.getCity())
                    .complement(addressDto.getComplement())
                    .street(addressDto.getStreet())
                    .nameUser(user.get().getFullName())
                    .user(user.get())
                    .build();
        }

    }
}

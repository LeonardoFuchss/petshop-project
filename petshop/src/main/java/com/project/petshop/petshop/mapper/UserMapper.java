package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.model.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDto dto) {

        return User.builder()
                .userCpf(dto.getUserCpf())
                .fullName(dto.getFullName())
                .profile(dto.getProfile())
                .password(dto.getPassword())
                .signUpDate(dto.getSignUpDate())
                .build();
    }
}

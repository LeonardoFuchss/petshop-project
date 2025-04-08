package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.domain.enums.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User toEntity(UserDto dto) {

        Profile profileEnum =  Profile.valueOf(dto.getProfile().toUpperCase());
        return User.builder()
                .userCpf(dto.getUserCpf())
                .fullName(dto.getFullName())
                .profile(profileEnum)
                .password(dto.getPassword())
                .signUpDate(LocalDateTime.now())
                .build();
    }
}

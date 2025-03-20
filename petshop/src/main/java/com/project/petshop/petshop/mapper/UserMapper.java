package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.model.enums.Profile;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDto dto) {

        Profile profileEnum =  Profile.valueOf(dto.getProfile().toUpperCase());
        return User.builder()
                .userCpf(dto.getUserCpf())
                .fullName(dto.getFullName())
                .profile(profileEnum)
                .password(dto.getPassword())
                .build();
    }
}

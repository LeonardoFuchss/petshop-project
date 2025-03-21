package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.domain.enums.Profile;
import com.project.petshop.petshop.dto.RegisterDto;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

     public User toEntity(RegisterDto registerDto) {

         String profile = "CLIENT";
         Profile profileSave =  Profile.valueOf(profile);

         return User.builder()
                 .userCpf(registerDto.getUserCpf())
                 .fullName(registerDto.getFullName())
                 .password(registerDto.getPassword())
                 .profile(profileSave)
                 .build();
     }

}

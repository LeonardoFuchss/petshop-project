package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.model.entities.enums.Profile;
import com.project.petshop.petshop.dto.RegisterDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
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

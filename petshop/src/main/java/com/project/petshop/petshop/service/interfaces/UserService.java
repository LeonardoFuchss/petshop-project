package com.project.petshop.petshop.service.interfaces;

import com.project.petshop.petshop.dto.AuthDto;
import com.project.petshop.petshop.dto.RegisterDto;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.domain.AccessToken;
import com.project.petshop.petshop.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(UserDto userdTO);
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUserCpf(String cpf);
    void delete(Long id);
    AccessToken authenticate(String cpf, String password);
    User register(RegisterDto registerDto);
    UserDetails getAuthenticatedUser();
}

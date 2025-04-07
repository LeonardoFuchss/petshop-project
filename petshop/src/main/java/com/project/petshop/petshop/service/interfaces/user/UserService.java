package com.project.petshop.petshop.service.interfaces.user;

import com.project.petshop.petshop.dto.RegisterDto;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.domain.AccessToken;
import com.project.petshop.petshop.domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(UserDto userdTO);
    List<User> findAll();
    User findById(Long id);
    void delete(Long id);
    Optional<User> findByUserCpf(String cpf);
    AccessToken authenticate(String cpf, String password);
    User register(RegisterDto registerDto);
    User updateUser(UserDto userDto);
}

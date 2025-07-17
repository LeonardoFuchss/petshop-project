package com.project.petshop.petshop.service.interfaces.user;

import com.project.petshop.petshop.dto.RegisterDto;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.model.entities.AccessToken;
import com.project.petshop.petshop.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(UserDto userdTO);
    List<User> findAllUsers();
    void deleteUserByCpf(String cpf);
    Optional<User> findUserByCpf(String cpf);
    AccessToken authenticate(String cpf, String password);
    User publicUserRegistration(RegisterDto registerDto);
    User updateUser(UserDto userDto);
}

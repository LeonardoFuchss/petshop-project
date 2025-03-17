package com.project.petshop.petshop.service.interfaces;

import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public void save(UserDto userdTO);
    public List<User> findAll();
    public Optional<User> findById(Long id);
    public void delete(Long id);
}

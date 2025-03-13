package com.project.petshop.petshop.service.interfaces;

import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.model.entities.User;

import java.util.List;

public interface UserService {
    public void save(UserDto userdTO);
    public List<UserDto> findAll();
    public UserDto findById(int id);
    public void delete(int id);
}

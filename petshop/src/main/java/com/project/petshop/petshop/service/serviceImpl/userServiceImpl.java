package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.service.interfaces.UserService;

import java.util.List;

public class userServiceImpl implements UserService {
    @Override
    public void save(UserDto userdTO) {

    }

    @Override
    public List<UserDto> findAll() {
        return List.of();
    }

    @Override
    public UserDto findById(int id) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}

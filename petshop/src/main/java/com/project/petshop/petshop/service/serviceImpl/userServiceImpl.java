package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.exceptions.user.UserAlreadyException;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.mapper.UserMapper;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserServiceImpl() {
    }

    @Override
    public User save(UserDto userdTO) {
        if (userRepository.findByUserCpf(userdTO.getUserCpf()).isPresent()) {
            throw new UserAlreadyException("User already exists");
        }
        User user = userMapper.toEntity(userdTO);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users not found");
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        user.ifPresent(value -> userRepository.delete(value));
    }
}

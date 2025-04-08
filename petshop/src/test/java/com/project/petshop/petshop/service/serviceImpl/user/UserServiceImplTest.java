package com.project.petshop.petshop.service.serviceImpl.user;

import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.service.interfaces.user.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    UserService userService;
    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("User created successfully")
    void saveSuccess() {
        UserDto userDto = new UserDto(
                "05416199008",
                "Leonardo Dos Santos Fuchs",
                "testeSenha10",
                "2025-05-08T10:57:27.8994019");

        User userSaved = userService.save(userDto);
        assertEquals("Leonardo Dos Santos Fuchs", userSaved.getFullName());
        User user = entityManager.find(User.class, userSaved.getId());
        assertNotNull(user);
    }
}
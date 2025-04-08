package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.dto.UserDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") /* Anotação para o spring utilizar a configuração do banco de testes */
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("Should get user successfully from database.")
    void findUserByUsernameSuccess() {
        UserDto userDto = new UserDto("05416199008", "Leonardo Dos Santos Fuchs", "testeSenha10", "2025-05-08T10:57:27.8994019");
        this.createUser(userDto);
        Optional<User> userResult = this.userRepository.findByFullName(userDto.getFullName());
        assertThat(userResult.isPresent()).isTrue();
    }
    @Test
    @DisplayName("Should not get user from database when user not exist.")
    void findUserByUsernameFailure() {
        String userName = "Andrielli Fraga da Silva";
        Optional<User> userResult = this.userRepository.findByFullName(userName);
        assertThat(userResult.isEmpty()).isTrue();
    }

    private void createUser(UserDto userDto) {
        User newUser = new User(userDto);
        this.entityManager.persist(newUser);
    }
}
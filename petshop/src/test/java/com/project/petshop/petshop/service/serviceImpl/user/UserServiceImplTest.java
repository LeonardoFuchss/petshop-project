package com.project.petshop.petshop.service.serviceImpl.user;

import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.domain.enums.Profile;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.exceptions.user.UserAlreadyException;
import com.project.petshop.petshop.mapper.UserMapper;
import com.project.petshop.petshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class) /* Para habilitar os mocks com mockito. */
class UserServiceImplTest {


    /**
     * "Mock" é usado para injetar os métodos no qual o 'userServiceImpl' possui dependência.
     * InjectMocks serve para injetar os mocks definidos, dentro da classe userServiceImpl.
     */
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;


    @Nested
    class saveUser {
        @Test
        @DisplayName("User created successfully")
        void shouldSaveUserWithSuccess() {
            User user = new User(1L, "05416199008", Profile.ADMIN, "Leonardo Dos Santos Fuchs", "testeSenha10", LocalDateTime.now());
            UserDto userDto = new UserDto("05416199008", "ADMIN", "Leonardo Dos Santos Fuchs", "testeSenha10");

            when(userRepository.findByUserCpf(any())).thenReturn(Optional.empty());
            when(userRepository.findByFullName(any())).thenReturn(Optional.empty());
            when(userMapper.toEntity(userDto)).thenReturn(user);
            doReturn(user).when(userRepository).save(any());
            when(passwordEncoder.encode(any())).thenReturn("senhaTeste10");

            User userSaved = userServiceImpl.save(userDto);

            assertNotNull(userSaved);
            assertEquals("Leonardo Dos Santos Fuchs", userSaved.getFullName());
            assertEquals("senhaTeste10", userSaved.getPassword());
        }
        @Test
        @DisplayName("User conflict")
        void shouldShowConflictException() {
            User user = new User(1L, "05416199008", Profile.ADMIN, "Leonardo Dos Santos Fuchs", "testeSenha10", LocalDateTime.now());
            UserDto userDto = new UserDto("05416199008", "ADMIN", "Leonardo Dos Santos Fuchs", "testeSenha10");

            when(userRepository.findByUserCpf(any())).thenReturn(Optional.of(user));
            when(userRepository.findByFullName(any())).thenReturn(Optional.of(user));

            assertThrows(UserAlreadyException.class, () -> userServiceImpl.save(userDto));
        }
    }
}
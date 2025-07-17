package com.project.petshop.petshop.service.impl.user;

import com.project.petshop.petshop.dto.RegisterDto;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.exceptions.user.UserAlreadyException;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.infra.jwt.service.JwtService;
import com.project.petshop.petshop.mapper.RegisterMapper;
import com.project.petshop.petshop.mapper.UserMapper;
import com.project.petshop.petshop.model.entities.AccessToken;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; /* Para criptografar a senha. */
    private final JwtService jwtService;
    private final RegisterMapper registerMapper;


    @Override
    @PreAuthorize("@authService.isAdmin()")
    public User createUser(UserDto userDto) {
        validateUserCpfNotExist(userDto.getUserCpf());
        validateFullNameNotExist(userDto.getFullName());
        alreadyEmailExist(userDto.getEmail());
        alreadyNumberExist(userDto.getNumber());
        User user = userMapper.toEntity(userDto);
        encodePassword(user);
        return userRepository.save(user);
    }

    private void encodePassword(User user) {
        String actualPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(actualPassword);
        user.setPassword(encodedPassword);
    }

    @Override
    @PreAuthorize("@authService.isAdmin()")
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        usersIsEmpty(users);
        return users;
    }

    @Override
    @PreAuthorize("@authService.isSelf(#cpf) or authService.isAdmin()")
    public void deleteUserByCpf(String cpf) {
        userRepository.findByUserCpf(cpf).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteUserByUserCpf(cpf);
    }

    @Override
    @PreAuthorize("@authService.isSelf(#cpf) or @authService.isAdmin()")
    public Optional<User> findUserByCpf(String cpf) {
        Optional<User> user = userRepository.findByUserCpf(cpf);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Invalid password or invalid CPF. try again");
        } else {
            return user;
        }
    }

    @Override
    public AccessToken authenticate(String cpf, String password) {
        Optional<User> user = userRepository.findByUserCpf(cpf);
        User userPresent = userFound(user);
        boolean matches = passwordEncoder.matches(password, userPresent.getPassword());
        if (matches) {
            return jwtService.generateToken(userPresent);
        } else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    @Override
    public User publicUserRegistration(RegisterDto registerDto) {
        validateUserCpfNotExist(registerDto.getUserCpf());
        User user = registerMapper.toEntity(registerDto);
        user.setSignUpDate(LocalDateTime.now());
        encodePassword(user);
        userRepository.save(user);
        return user;
    }

    @Override
    @PreAuthorize("@authService.isSelf(#userDto.userCpf) or @authService.isAdmin()")
    public User updateUser(UserDto userDto) {
        return saveUpdateUser(userDto);
    }

    private User saveUpdateUser(UserDto userDto) {
        Optional<User> userFound = getUserByCpf(userDto.getUserCpf());
        User userPresent = userFound(userFound);
        String password = passwordEncoder.encode(userDto.getPassword());
        userPresent.setUserCpf(userDto.getUserCpf());
        userPresent.setPassword(password);
        userPresent.setFullName(userDto.getFullName());
        return userRepository.save(userPresent);
    }
















    /*
     * *************************************************************************************************************
     */


    // Métodos reutilizáveis

    /**
     * Verifica se já existe um usuário para o CPF cadastrado.
     */
    private void validateUserCpfNotExist(String cpf) {
        Optional<User> user = userRepository.findByUserCpf(cpf);
        if (user.isPresent()) {
            throw new UserAlreadyException("This CPF is already registered. Login or try again!");
        }
    }

    /**
     * Busca o usuário com base no CPF.
     */
    private Optional<User> getUserByCpf(String cpf) {
        return Optional.ofNullable(userRepository.findByUserCpf(cpf).orElseThrow(() -> new UserNotFoundException("User not found. Please try again!")));
    }

    /**
     * Verifica se já existe um full name registrado.
     */
    private void validateFullNameNotExist(String fullName) {
        Optional<User> user = userRepository.findByFullName(fullName);
        userFound(user);
    }

    /**
     * Verifica se a lista de usuários está vazia.
     */
    private void usersIsEmpty(List<User> users) {
        if (users.isEmpty()) {
            throw new UserNotFoundException("No user has been found.");
        }
    }

    private User userFound(Optional<User> user) {
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found. Please try again.");
        }
        return user.get();
    }

    private void alreadyEmailExist(String email) {
        Optional<User> emailExist = userRepository.findByEmail(email);
        if (emailExist.isPresent()) {
            throw new UserAlreadyException("This email is already registered.");
        }
    }

    private void alreadyNumberExist(String number) {
        Optional<User> numberExist = userRepository.findByNumberContact(number);
        if (numberExist.isPresent()) {
            throw new UserAlreadyException("This contact number already exists.");
        }
    }
}

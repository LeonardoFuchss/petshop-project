package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.exceptions.user.UserAlreadyException;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.jwt.JwtService;
import com.project.petshop.petshop.mapper.UserMapper;
import com.project.petshop.petshop.model.AccessToken;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.model.enums.Profile;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; /* Para criptografar a senha. */

    @Autowired
    private JwtService jwtService;

    private LocalDateTime now = LocalDateTime.now();


    @Override
    public User save(UserDto userdTO) {
        if (userRepository.findByUserCpf(userdTO.getUserCpf()).isPresent()) { /* Verifica se o usuário existe */
            throw new UserAlreadyException("User already exists");
        }
        User user = userMapper.toEntity(userdTO); /* Mapeamento para entidade */
        user.setSignUpDate(now);

        encodePassword(user); /* Chama a função abaixo que criptografa a senha */
        userRepository.save(user); /* Persiste a entidade mapeada no banco de dados */
        return user;
    }

    private void encodePassword(User user) { /* Recebe um usuário e criptografa a senha. */
        String actualPassword = user.getPassword(); /* Pega a senha atual do usuário */
        String encodedPassword = passwordEncoder.encode(actualPassword); /* Coloca ela como criptografada. */
        user.setPassword(encodedPassword); /* Atualiza a senha do usuário. */
    }


    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll(); /* Busca uma lista de usuários no banco de dados */
        if (users.isEmpty()) { /* Verifica se a lista está vazia. */
            throw new UserNotFoundException("Users not found");
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id); /* Busca um usuário com base no identificador */
        if (user.isEmpty()) { /* Verifica se a consulta retornou vazia. */
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public Optional<User> findByUserCpf(String cpf) {
        return findByUserCpf(cpf);
    }

    @Override
    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id); /* Busca um usuário com base no seu identificador */
        if (user.isEmpty()) { /* Verifica se a consulta retornou vazia */
            throw new UserNotFoundException("User not found");
        }
        user.ifPresent(value -> userRepository.delete(value)); /* Se retornou um usuário, deleta ele do banco de dados. */
    }

    @Override
    public AccessToken authenticate(String cpf, String password) { /* Autenticação do usuário retornando um token de acesso (Recebe as credenciais no parâmetro) */

        Optional<User> user = userRepository.findByUserCpf(cpf); /* Busca o usuário pelo seu cpf. */

        if (user.isEmpty()) { /* Verifica se o usuário existe. */
            throw new UserNotFoundException("User not found");
        }

        boolean matches = passwordEncoder.matches(password, user.get().getPassword()); /* Variável que verifica se a senha passada por parâmetro é igual a senha do usuário no banco. */

        if (matches) { /* se for igual, retorna um token com o método que gera o token. */
            return jwtService.generateToken(user.get());
        } else {
            return null; /* se não for igual, retorna null*/
        }
    }
}

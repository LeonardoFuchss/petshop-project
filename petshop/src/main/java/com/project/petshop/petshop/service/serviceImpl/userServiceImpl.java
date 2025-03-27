package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.AuthDto;
import com.project.petshop.petshop.dto.RegisterDto;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.exceptions.user.UserAlreadyException;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.jwt.JwtService;
import com.project.petshop.petshop.mapper.RegisterMapper;
import com.project.petshop.petshop.mapper.UserMapper;
import com.project.petshop.petshop.domain.AccessToken;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Objects;
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
    @Autowired
    private RegisterMapper registerMapper;


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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users not found");
        }
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) { /* Verifica se a lista está vazia. */
            return users;
        } else {
            throw new UnauthorizedException("You do not have permission to access this resource");
        }
    }

    @Override
    public Optional<User> findById(Long id) { /* Busca pelo id (Apenas admins que visualizam qualquer cadastro) */

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); /* Buscando usuário autenticado no contexto do spring. */
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found")); /* Buscando usuário no banco de dados */

        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) { /* Se o role for admin, retorna o usuário */
            return Optional.of(user);
        } else {
            if (Objects.equals(user.getUserCpf(), userDetails.getUsername())) { /* Caso contrário, verifica se o usuário autenticado é igual ao usuário no banco de dados e retorna o usuário. (userName é o userCpf definido na autenticação) */
                return Optional.of(user);
            } else {
                throw new UnauthorizedException("You are not allowed to access this user.");
            }
        }
    }

    @Override
    public Optional<User> findByUserCpf(String cpf) {
        return userRepository.findByUserCpf(cpf);
    }

    @Override
    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id); /* Busca um usuário com base no seu identificador */
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.isEmpty()) { /* Verifica se a consulta retornou vazia */
            throw new UserNotFoundException("User not found");
        }

        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            user.ifPresent(userRepository::delete);
        } else if (userDetails.getUsername().equals(user.get().getUserCpf())) {
            user.ifPresent(userRepository::delete);
        } else {
            throw new UnauthorizedException("You are not allowed to delete this user.");
        }
    }

    @Override
    public AccessToken authenticate(String cpf, String password) { /* Autenticação do usuário retornando um token de acesso (Recebe as credenciais no parâmetro) */
        Optional<User> user = userRepository.findByUserCpf(cpf); /* Busca o usuário pelo seu cpf. */
        if (user.isEmpty()) { /* Verifica se o usuário existe. */
            throw new UserNotFoundException("Invalid password or invalid CPF. try again");
        }
        boolean matches = passwordEncoder.matches(password, user.get().getPassword()); /* Variável que verifica se a senha passada por parâmetro é igual a senha do usuário no banco. */
        if (matches) { /* se for igual, retorna um token com o método que gera o token. */
            return jwtService.generateToken(user.get());
        } else {
            return null; /* se não for igual, retorna null*/
        }
    }

    @Override
    public User register(RegisterDto registerDto) {
        if (userRepository.findByUserCpf(registerDto.getUserCpf()).isPresent()) { /* Verifica se o usuário existe */
            throw new UserAlreadyException("User already exists");
        }
        User user = registerMapper.toEntity(registerDto); /* Mapeamento para entidade */
        user.setSignUpDate(now);

        encodePassword(user); /* Chama a função abaixo que criptografa a senha */
        userRepository.save(user); /* Persiste a entidade mapeada no banco de dados */
        return user;
    }

    @Override
    public UserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }
}

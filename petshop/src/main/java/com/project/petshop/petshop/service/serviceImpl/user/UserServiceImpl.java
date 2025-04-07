package com.project.petshop.petshop.service.serviceImpl.user;

import com.project.petshop.petshop.dto.RegisterDto;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.exceptions.user.UserAlreadyException;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.infra.jwt.service.JwtService;
import com.project.petshop.petshop.mapper.RegisterMapper;
import com.project.petshop.petshop.mapper.UserMapper;
import com.project.petshop.petshop.domain.AccessToken;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; /* Para criptografar a senha. */
    private final JwtService jwtService;
    private final RegisterMapper registerMapper;


    /**
     * Persiste um novo usuário no banco de dados.
     * .
     * Recebe um dto no parâmetro e com base no CPF e o NOME verifica se o usuário existe.
     * Mapeamento de DTO para ENTIDADE.
     * Seta a data e hora de login com base na data e hora atual (now: Definido no atributo da classe).
     * Criptografa a senha do usuário utilizando o método criado para criptografar (encodePassword).
     * Chama o save do userRepository para salvar um novo usuário.
     */
    @Override
    public User save(UserDto userdTO) {
        Optional<User> userFound = userByCpf(userdTO.getUserCpf());
        Optional<User> userFoundByName = userByFullName(userdTO.getFullName());
        if (userFound.isPresent() || userFoundByName.isPresent()) { /* OU */
            throw new UserAlreadyException("User already exists");
        }
        User user = userMapper.toEntity(userdTO);
        user.setSignUpDate(LocalDateTime.now());
        encodePassword(user);
        userRepository.save(user);
        return user;
    }

    /**
     * Criptografando a senha do usuário Utilizando passwordEncoder
     */
    private void encodePassword(User user) {
        String actualPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(actualPassword);
        user.setPassword(encodedPassword);
    }

    /**
     * Retorna uma lista de todos os usuários do banco de dados.
     * .
     * Utilizamos UserDetails para buscar o usuário autenticado no contexto do spring.
     * Variável boolean para verificar se o usuário autenticado possui role ADMIN. (Role necessário para ter permissão de visualizar todos os usuários)
     */
    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        UserDetails userDetails = userAuth();
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users not found");
        }
        if (isAdmin) {
            return users;
        } else {
            throw new UnauthorizedException("You do not have permission to view all users.");
        }
    }

    /**
     * Busca um usuário com base no ID passado por parâmetro.
     * .
     * Utiliza UserDetails para recuperar o usuário autenticado no contexto do spring.
     * Verifica se o Role do usuário autenticado é ROLE_ADMIN
     * Verifica se o CPF do usuário é igual ao UserName do UserDetails (CPF é definido como user name)
     */
    @Override
    public User findById(Long id) {
        UserDetails userDetails = userAuth();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return user;
        } else {
            if (Objects.equals(user.getUserCpf(), userDetails.getUsername())) {
                return user;
            } else {
                throw new UnauthorizedException("You are not allowed to access this user.");
            }
        }
    }

    /**
     * Deleta o usuário recuperado pelo ID.
     * UserDetails para recuperar usuário autenticado e verificar role de permissão
     */
    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserDetails userDetails = userAuth();
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            userRepository.delete(user);
        } else if (userDetails.getUsername().equals(user.getUserCpf())) {
            userRepository.delete(user);
        } else {
            throw new UnauthorizedException("You are not allowed to delete this user.");
        }
    }

    @Override
    public Optional<User> findByUserCpf(String cpf) {
        return userRepository.findByUserCpf(cpf);
    }

    /**
     * Autenticação de usuário
     * Verifica se o usuário existe pelo CPF
     * Verifica se a senha bate com a senha criptografada no banco (matches)
     * Retorna um AccessToken gerado através do JwtService ((string) utilizado para autenticar usuário).
     */
    @Override
    public AccessToken authenticate(String cpf, String password) {
        Optional<User> user = userByCpf(cpf);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Invalid password or invalid CPF. try again");
        }
        boolean matches = passwordEncoder.matches(password, user.get().getPassword());
        if (matches) {
            return jwtService.generateToken(user.get());
        } else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    /**
     * Persiste um novo usuário no banco de dados.
     * .
     * Recebe um dto no parâmetro e com base no CPF e o NOME verifica se o usuário existe.
     * Mapeamento de DTO para ENTIDADE.
     * Seta a data e hora de login com base na data e hora atual (now: Definido no atributo da classe).
     * Criptografa a senha do usuário utilizando o método criado para criptografar (encodePassword).
     * Chama o save do userRepository para salvar um novo usuário.
     */
    @Override
    public User register(RegisterDto registerDto) {
        if (userByCpf(registerDto.getUserCpf()).isPresent()) {
            throw new UserAlreadyException("User already exists");
        }
        User user = registerMapper.toEntity(registerDto);
        user.setSignUpDate(LocalDateTime.now());
        encodePassword(user);
        userRepository.save(user);
        return user;
    }

    /**
     * Atualiza dados do usuário.
     * Recupera usuário autenticado no contexto do spring.
     */
    @Override
    public User updateUser(UserDto userDto) {
        UserDetails userDetails = userAuth();
        return saveUpdateUser(userDetails, userDto);
    }

    /**
     * Verifica se o usuário possui role admin ou se o CPF é igual o utilizado na autenticação.
     */
    private boolean isAuthorized(UserDetails userDetails, String cpf) {
        return userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
                || userDetails.getUsername().equals(cpf);
    }

    /**
     * Lógica para atualizar os dados do usuário.
     */
    private User saveUpdateUser(UserDetails userAuth, UserDto userDto) { /* Salva usuário no banco */
        if (isAuthorized(userAuth, userDto.getUserCpf())) { /* se for autorizado */
            Optional<User> userFound = userByCpf(userDto.getUserCpf());
            String password = passwordEncoder.encode(userDto.getPassword());
            if (userFound.isPresent()) {
                userFound.get().setUserCpf(userDto.getUserCpf());
                userFound.get().setPassword(password);
                userFound.get().setFullName(userDto.getFullName());
                return userRepository.save(userFound.get());
            } else {
                throw new UserNotFoundException("User not found");
            }
        } else {
            throw new UnauthorizedException("You are not allowed to access this user.");
        }
    }


    // Métodos abstratos

    /**
     * Recupera usuário pelo CPF.
     */
    private Optional<User> userByCpf(String cpf) {
        return userRepository.findByUserCpf(cpf);
    }
    /**
     * Recupera usuário pelo nome completo.
     */
    private Optional<User> userByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }
    /**
     * Recupera usuário autenticado no contexto do spring.
     */
    private UserDetails userAuth() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

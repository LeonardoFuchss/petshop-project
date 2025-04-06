package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.domain.enums.Profile;
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
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; /* Para criptografar a senha. */
    private final JwtService jwtService;
    private final LocalDateTime now = LocalDateTime.now(); /* Data e hora atual */
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
        Optional<User> userFound = userRepository.findByUserCpf(userdTO.getUserCpf());
        Optional<User> userFoundByName = userRepository.findByFullName(userdTO.getFullName());
        if (userFound.isPresent() || userFoundByName.isPresent()) {
            throw new UserAlreadyException("User already exists");
        }
        User user = userMapper.toEntity(userdTO);
        user.setSignUpDate(now);
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users not found");
        }
        if (isAdmin) {
            return users;
        } else {
            throw new UnauthorizedException("You are not an admin");
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
    public Optional<User> findById(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return Optional.of(user);
        } else {
            if (Objects.equals(user.getUserCpf(), userDetails.getUsername())) {
                return Optional.of(user);
            } else {
                throw new UnauthorizedException("You are not allowed to access this user.");
            }
        }
    }

    /**
     * Deleta o usuário recuperado pelo ID.
     * UserDetails para recuperar usuário autenticado e verificar role de permissão
     *
     */
    @Override
    public void delete(Long id) {
        Optional<User> user = Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found")));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            user.ifPresent(userRepository::delete);
        } else if (userDetails.getUsername().equals(user.get().getUserCpf())) {
            user.ifPresent(userRepository::delete);
        } else {
            throw new UnauthorizedException("You are not allowed to delete this user.");
        }
    }

    /**
     * Autenticação de usuário
     * Verifica se o usuário existe pelo CPF
     * Verifica se a senha bate com a senha criptografada no banco (matches)
     * Retorna um AccessToken gerado através do JwtService ((string) utilizado para autenticar usuário).
     */
    @Override
    public AccessToken authenticate(String cpf, String password) {
        Optional<User> user = userRepository.findByUserCpf(cpf);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Invalid password or invalid CPF. try again");
        }
        boolean matches = passwordEncoder.matches(password, user.get().getPassword());
        if (matches) {
            return jwtService.generateToken(user.get());
        } else {
            return null;
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
        if (userRepository.findByUserCpf(registerDto.getUserCpf()).isPresent()) {
            throw new UserAlreadyException("User already exists");
        }
        User user = registerMapper.toEntity(registerDto);
        user.setSignUpDate(now);
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    private User saveUpdateUser(UserDetails user, UserDto userDto) { /* Salva usuário no banco */
        if (isAuthorized(user, userDto.getUserCpf())) { /* se for autorizado */
            Optional<User> userFound = userRepository.findByUserCpf(userDto.getUserCpf());
            if (userFound.isPresent()) {
                userFound.get().setUserCpf(userDto.getUserCpf());
                userFound.get().setPassword(userDto.getPassword());
                userFound.get().setFullName(userDto.getFullName());
                return userRepository.save(userFound.get());
            } else {
                throw new UserNotFoundException("User not found");
            }
        } else {
            throw new UnauthorizedException("You are not allowed to access this user.");
        }
    }
}

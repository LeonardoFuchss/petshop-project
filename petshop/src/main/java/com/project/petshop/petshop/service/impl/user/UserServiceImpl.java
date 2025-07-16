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
    public User createUser(UserDto userDto) {
        validateUserCpfNotExist(userDto.getUserCpf());
        validateFullNameNotExist(userDto.getFullName());
        alreadyEmailExist(userDto.getEmail());
        alreadyNumberExist(userDto.getNumber());
        User user = userMapper.toEntity(userDto);
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
    public List<User> findAllUsers() {
        UserDetails userDetails = getUserAuth();
        isAdmin(userDetails);
        List<User> users = userRepository.findAll();
        usersIsEmpty(users);
        return users;
    }

    /**
     * Busca um usuário com base no ID passado por parâmetro.
     * .
     * Utiliza UserDetails para recuperar o usuário autenticado no contexto do spring.
     * Verifica se o Role do usuário autenticado é ROLE_ADMIN
     * Verifica se o CPF do usuário é igual ao UserName do UserDetails (CPF é definido como user name)
     */
    @Override
    public Optional<User> findUserById(Long id) {
        UserDetails userDetails = getUserAuth();
        Optional<User> user = Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found. Please try again.")));
        User userPresent = userFound(user);
        userAuthOrIsAdmin(userDetails, userPresent.getUserCpf());
        return user;
    }

    /**
     * Deleta o usuário recuperado pelo ID.
     * UserDetails para recuperar usuário autenticado e verificar role de permissão
     */
    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserDetails userDetails = getUserAuth();
        userAuthOrIsAdmin(userDetails, user.getUserCpf());
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findUserByCpf(String cpf) {
        Optional<User> user = userRepository.findByUserCpf(cpf);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Invalid password or invalid CPF. try again");
        } else {
            return user;
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
        Optional<User> user = findUserByCpf(cpf);
        User userPresent = userFound(user);
        boolean matches = passwordEncoder.matches(password, userPresent.getPassword());
        if (matches) {
            return jwtService.generateToken(userPresent);
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
    public User publicUserRegistration(RegisterDto registerDto) {
        validateUserCpfNotExist(registerDto.getUserCpf());
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
        UserDetails userDetails = getUserAuth();
        return saveUpdateUser(userDetails, userDto);
    }

    /**
     * Lógica para atualizar os dados do usuário.
     */
    private User saveUpdateUser(UserDetails userAuth, UserDto userDto) { /* Salva usuário no banco */
        /* se for autorizado */
        userAuthOrIsAdmin(userAuth, userDto.getUserCpf());
        Optional<User> userFound = getUserByCpf(userDto.getUserCpf());
        User userPresent = userFound(userFound);
        String password = passwordEncoder.encode(userDto.getPassword());
        userPresent.setUserCpf(userDto.getUserCpf());
        userPresent.setPassword(password);
        userPresent.setFullName(userDto.getFullName());
        return userRepository.save(userPresent);
    }


    /**
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
        if (user.isPresent()) {
            throw new UserAlreadyException("This name is already registered. Login or try again!");
        }
    }

    /**
     * Recupera o Usuário autenticado no contexto de segurança do spring.
     */
    private UserDetails getUserAuth() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Verifica se o usuário é admin.
     */
    private void isAdmin(UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new UnauthorizedException("You are not allowed to access this resource. Please login with admin account or contact the admin to register you as an admin account.");
        }
    }

    /**
     * Verifica se o usuário está autenticado ou se é admin.
     */
    private void userAuthOrIsAdmin(UserDetails userDetails, String cpf) {
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !Objects.equals(userDetails.getUsername(), cpf)) {
            throw new UnauthorizedException("You are not allowed to access this resource. Please login with admin account or contact the admin to register you as an admin account.");
        }
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
    private void alreadyNumberExist(String number){
        Optional<User> numberExist = userRepository.findByNumberContact(number);
        if (numberExist.isPresent()) {
            throw new UserAlreadyException("This contact number already exists.");
        }
    }
}

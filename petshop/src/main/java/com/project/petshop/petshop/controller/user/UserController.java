package com.project.petshop.petshop.controller.user;

import com.project.petshop.petshop.dto.AuthDto;
import com.project.petshop.petshop.dto.RegisterDto;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.service.interfaces.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/save")
    @Operation(description = "Persiste um novo usuário no banco de dados. (acesso restrito para admins)")
    public ResponseEntity<User> save(@Valid @RequestBody UserDto userdTO) {
        User user = userService.createUser(userdTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @PostMapping("/register")
    @Operation(description = "Persiste um novo usuário no banco de dados. (cadastro público)")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        User user = userService.publicUserRegistration(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @PostMapping("/authenticate")
    @Operation(description = "Autenticação de usuário. (LOGIN)")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthDto authDTO) { /* EndPoint para autenticação do usuário */
        var token = userService.authenticate(authDTO.getCpf(), authDTO.getPassword());
        if (token == null) {
            throw new UnauthorizedException("Invalid credentials");
        } else {
            return ResponseEntity.ok(token);
        }
    }

    @GetMapping("/findAll")
    @Operation(description = "Busca os usuários cadastrados.")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete/{cpf}")
    @Operation(description = "Deleta um usuário com base no seu ID.")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        userService.deleteUserByCpf(cpf);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/update")
    @Operation(description = "Atualiza o usuário no banco de dados.")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserDto userDto) {
        User userSaved = userService.updateUser(userDto);
        return ResponseEntity.ok(userSaved);
    }
}

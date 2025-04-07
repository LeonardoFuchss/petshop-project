package com.project.petshop.petshop.controller.user;

import com.project.petshop.petshop.dto.AuthDto;
import com.project.petshop.petshop.dto.RegisterDto;
import com.project.petshop.petshop.dto.UserDto;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.service.interfaces.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/save")
    @Operation(description = "Persiste um novo usuário no banco de dados. (acesso restrito para admins)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorna o usuário criado."),
            @ApiResponse(responseCode = "409", description = "Erro em caso de conflito."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<?> save(@Valid @RequestBody UserDto userdTO) {
        User user = userService.save(userdTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @PostMapping("/register")
    @Operation(description = "Persiste um novo usuário no banco de dados. (cadastro público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorna o usuário criado."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        User user = userService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @PostMapping("/authenticate")
    @Operation(description = "Autenticação de usuário. (LOGIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o token de acesso no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Erro de credencial inválida."),
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista de usuários cadastrados."),
            @ApiResponse(responseCode = "404", description = "Erro caso não exista usuários cadastrados."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }



    @GetMapping("/find/{id}")
    @Operation(description = "Busca um usuário específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o usuário encontrado."),
            @ApiResponse(responseCode = "404", description = "Erro caso o usuário informado não exista."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }



    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um usuário com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem retorno. Porém, a requisição foi bem sucedida!"),
            @ApiResponse(responseCode = "404", description = "Erro caso o usuário informado não exista."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserDto userDto) {
        User userSaved = userService.updateUser(userDto);
        return ResponseEntity.ok(userSaved);
    }
}

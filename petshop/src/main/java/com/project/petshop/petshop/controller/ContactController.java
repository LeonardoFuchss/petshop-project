package com.project.petshop.petshop.controller;

import com.project.petshop.petshop.dto.ContactDto;
import com.project.petshop.petshop.domain.entities.Contact;
import com.project.petshop.petshop.service.interfaces.ContactService;
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
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/save")
    @Operation(description = "Persiste um novo contato no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contato criado com sucesso."),
            @ApiResponse(responseCode = "409", description = "Erro em caso de conflito."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<Void> addContact(@Valid @RequestBody ContactDto contactDto) {
        contactService.save(contactDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/findAll")
    @Operation(description = "Busca uma lista de contatos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista de contatos cadastrados."),
            @ApiResponse(responseCode = "404", description = "Erro em caso de nenhum registro encontrado."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<List<Contact>> findAll() {
        return ResponseEntity.ok().body(contactService.findAll());
    }


    @GetMapping("/find/{id}")
    @Operation(description = "Busca um contato específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o contato encontrado."),
            @ApiResponse(responseCode = "404", description = "Erro caso o contato informado não tenha registro no banco de dados."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Optional<Contact>> findById(@PathVariable Long id) {
        Optional<Contact> contact = contactService.findById(id);
        return ResponseEntity.ok(contact);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um contato com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem retorno. Porém, a requisição foi bem sucedida!"),
            @ApiResponse(responseCode = "404", description = "Erro caso o contato informado não exista."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.project.petshop.petshop.controller;

import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.domain.entities.Address;
import com.project.petshop.petshop.service.interfaces.AddressService;
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
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/save")
    @Operation(description = "Persiste um novo address no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address criado com sucesso."),
            @ApiResponse(responseCode = "409", description = "Erro em caso de conflito."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<Address> save(@Valid @RequestBody AddressDto addressDto) {
        Address address = addressService.save(addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }


    @GetMapping("/findAll")
    @Operation(description = "Busca uma lista de appointments cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista de address cadastrados."),
            @ApiResponse(responseCode = "404", description = "Erro em caso de nenhum registro encontrado."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<List<Address>> findAll() {
        return ResponseEntity.ok(addressService.findAll());
    }


    @GetMapping("/find/{id}")
    @Operation(description = "Busca um address específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o address encontrado."),
            @ApiResponse(responseCode = "404", description = "Erro caso o address informado não tenha registro no banco de dados."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Optional<Address>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.findById(id));
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um address com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem retorno. Porém, a requisição foi bem sucedida!"),
            @ApiResponse(responseCode = "404", description = "Erro caso o address informado não exista."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

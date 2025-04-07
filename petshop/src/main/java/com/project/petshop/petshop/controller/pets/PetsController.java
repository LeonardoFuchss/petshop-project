package com.project.petshop.petshop.controller.pets;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.domain.entities.Pets;
import com.project.petshop.petshop.service.interfaces.pets.PetsService;
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
@RequestMapping("/pets")
public class PetsController {
    @Autowired
    private PetsService petsService;


    @PostMapping("/save")
    @Operation(description = "Persiste um novo pet no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorna o pet criado."),
            @ApiResponse(responseCode = "409", description = "Erro em caso de conflito."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<?> save(@Valid @RequestBody PetsDto petsDto) {
        Pets pets = petsService.save(petsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pets);
    }


    @GetMapping("/findAll")
    @Operation(description = "Busca uma lista de pets cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista de pets cadastrados."),
            @ApiResponse(responseCode = "404", description = "Erro em caso de nenhum registro encontrado."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<List<Pets>> findAll() {
        return ResponseEntity.ok(petsService.findAll());
    }


    @GetMapping("/find/{id}")
    @Operation(description = "Busca um pet específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o pet encontrado."),
            @ApiResponse(responseCode = "404", description = "Erro caso o pet informado não tenha registro no banco de dados."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Pets> findById(@PathVariable Long id) {
        return ResponseEntity.ok(petsService.findById(id));
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um pet com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem retorno. Porém, a requisição foi bem sucedida!"),
            @ApiResponse(responseCode = "404", description = "Erro caso o pet informado não exista."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Pets> update(@Valid @RequestBody PetsDto petsDto) {
        Pets petsSaved = petsService.update(petsDto);
        return ResponseEntity.ok(petsSaved);
    }
}

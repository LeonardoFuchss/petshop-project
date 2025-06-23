package com.project.petshop.petshop.controller.breed;

import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.domain.entities.Breed;
import com.project.petshop.petshop.service.interfaces.breeds.BreedService;
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
@RequestMapping("/breed")
public class BreedController {

    @Autowired
    private BreedService breedService;

    @PostMapping("/save")
    @Operation(description = "Persiste um novo breed no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Breed criado com sucesso."),
            @ApiResponse(responseCode = "409", description = "Erro em caso de conflito."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<?> save(@Valid @RequestBody BreedDto breedDto) {
        Breed breed = breedService.createBreed(breedDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(breed);
    }


    @GetMapping("/findAll")
    @Operation(description = "Busca uma lista de breeds cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista de breeds cadastrados."),
            @ApiResponse(responseCode = "404", description = "Erro em caso de nenhum registro encontrado."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<List<Breed>> findAll() {
        return ResponseEntity.ok(breedService.findAllBreeds());
    }


    @GetMapping("/find/{id}")
    @Operation(description = "Busca um breed específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o breed encontrado."),
            @ApiResponse(responseCode = "404", description = "Erro caso o breed informado não tenha registro no banco de dados."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Optional<Breed>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(breedService.findBreedById(id));
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um breed com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem retorno. Porém, a requisição foi bem sucedida!"),
            @ApiResponse(responseCode = "404", description = "Erro caso o breed informado não exista."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        breedService.deleteBreedById(id);
        return ResponseEntity.noContent().build();
    }
}

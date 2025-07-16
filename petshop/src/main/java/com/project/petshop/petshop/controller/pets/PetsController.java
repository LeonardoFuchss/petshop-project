package com.project.petshop.petshop.controller.pets;

import com.project.petshop.petshop.dto.PetDtoPublic;
import com.project.petshop.petshop.model.entities.Pet;
import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.service.interfaces.pets.PetsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@AllArgsConstructor
public class PetsController {

    private final PetsService petsService;


    @PostMapping("/save")
    @Operation(description = "Persiste um novo pet no banco de dados. (Admins)")
    public ResponseEntity<?> save(@Valid @RequestBody PetsDto petsDto) {
        Pet pet = petsService.createPet(petsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pet);
    }
    @PostMapping("/savePublic")
    @Operation(description = "Persiste um novo pet no banco de dados. (Publico)")
    public ResponseEntity<?> savePublic(@Valid @RequestBody PetDtoPublic petsDtoPublic) {
        Pet pet = petsService.createPetPublic(petsDtoPublic);
        return ResponseEntity.status(HttpStatus.CREATED).body(pet);
    }


    @GetMapping("/findAll")
    @Operation(description = "Busca uma lista de pets cadastrados.")
    public ResponseEntity<List<Pet>> findAll() {
        return ResponseEntity.ok(petsService.findAllPets());
    }


    @GetMapping("/find/{id}")
    @Operation(description = "Busca um pet específico pelo seu ID.")
    public ResponseEntity<Pet> findById(@PathVariable Long id) {
        return ResponseEntity.ok(petsService.findPetById(id));
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um pet com base no seu ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petsService.deletePetById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update")
    @Operation(description = "Atualiza o Pet no banco de dados.")
    public ResponseEntity<Pet> update(@Valid @RequestBody PetsDto petsDto) {
        Pet petSaved = petsService.updatePet(petsDto);
        return ResponseEntity.ok(petSaved);
    }
}

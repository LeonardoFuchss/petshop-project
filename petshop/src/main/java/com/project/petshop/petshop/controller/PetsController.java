package com.project.petshop.petshop.controller;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.model.entities.Pets;
import com.project.petshop.petshop.service.interfaces.PetsService;
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
    public ResponseEntity<?> save(@Valid @RequestBody PetsDto petsDto) {
        Pets pets = petsService.save(petsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pets);
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<Pets>> findAll() {
        return ResponseEntity.ok(petsService.findAll());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<Pets>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(petsService.findById(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

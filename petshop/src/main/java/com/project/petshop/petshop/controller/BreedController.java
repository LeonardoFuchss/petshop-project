package com.project.petshop.petshop.controller;

import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.model.entities.Breed;
import com.project.petshop.petshop.service.interfaces.BreedService;
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
    public ResponseEntity<?> save(@Valid @RequestBody BreedDto breedDto) {
        Breed breed = breedService.save(breedDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(breed);
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<Breed>> findAll() {
        return ResponseEntity.ok(breedService.findAll());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<Breed>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(breedService.findById(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        breedService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

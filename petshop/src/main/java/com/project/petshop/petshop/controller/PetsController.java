package com.project.petshop.petshop.controller;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.model.entities.Pets;
import com.project.petshop.petshop.service.interfaces.PetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@CrossOrigin("*")
public class PetsController {
    @Autowired
    private PetsService petsService;

    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody PetsDto petsDto) {
        petsService.save(petsDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<Pets>> findAll() {
        return ResponseEntity.ok(petsService.findAll());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Pets> findById(@PathVariable Long id) {
        return ResponseEntity.ok(petsService.findById(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

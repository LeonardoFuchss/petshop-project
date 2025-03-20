package com.project.petshop.petshop.controller;

import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.model.entities.Address;
import com.project.petshop.petshop.service.interfaces.AddressService;
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
    public ResponseEntity<Void> save(@Valid @RequestBody AddressDto addressDto) {
        addressService.save(addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<Address>> findAll() {
        return ResponseEntity.ok(addressService.findAll());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Optional<Address>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.findById(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.project.petshop.petshop.controller.address;

import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.model.entities.Address;
import com.project.petshop.petshop.service.interfaces.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/save")
    @Operation(description = "Persiste um novo address no banco de dados.")
    public ResponseEntity<Address> save(@Valid @RequestBody AddressDto addressDto) {
        Address address = addressService.createAddress(addressDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }


    @GetMapping("/findAll")
    @Operation(description = "Busca uma lista de appointments cadastrados.")
    public ResponseEntity<List<Address>> findAll() {
        return ResponseEntity.ok(addressService.findAllAddress());
    }


    @GetMapping("/find/{id}")
    @Operation(description = "Busca um address específico pelo seu ID.")
    public ResponseEntity<Optional<Address>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.findAddressById(id));
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um address com base no seu ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.deleteAddressById(id);
        return ResponseEntity.noContent().build();
    }
}

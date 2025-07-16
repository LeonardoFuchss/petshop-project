package com.project.petshop.petshop.controller.serviceprovided;

import com.project.petshop.petshop.dto.ServiceProvidedDto;
import com.project.petshop.petshop.model.entities.ServiceProvided;
import com.project.petshop.petshop.service.interfaces.serviceprovided.ServiceProvicedService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("service")
@AllArgsConstructor
public class ServiceProvidedController {
    private final ServiceProvicedService service;

    @PostMapping("/create")
    public ResponseEntity<ServiceProvided> createService(@Valid @RequestBody ServiceProvidedDto serviceProvidedDto) {
        ServiceProvided serviceProvided = service.createService(serviceProvidedDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceProvided);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<ServiceProvided> findServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findServiceById(id));
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<ServiceProvided>> findAllServices() {
        return ResponseEntity.ok(service.findAllServices());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        service.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }
}

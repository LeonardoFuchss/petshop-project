package com.project.petshop.petshop.controller;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.service.interfaces.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@CrossOrigin("*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/save")
    public ResponseEntity<Void> save (@RequestBody AppointmentDto appointmentDto) {
        appointmentService.save(appointmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<Appointment>> findAll() {
        List<Appointment> appointments = appointmentService.findAll();
        return ResponseEntity.ok(appointments);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Appointment> findById(@PathVariable Long id) {
        Appointment appointment = appointmentService.findById(id);
        return ResponseEntity.ok(appointment);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        Appointment appointment = appointmentService.findById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package com.project.petshop.petshop.controller.appointment;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.dto.AppointmentUpdateDto;
import com.project.petshop.petshop.service.interfaces.appointment.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/save")
    @Operation(description = "Persiste um novo appointment no banco de dados.")
    public ResponseEntity<Appointment> save (@Valid @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.createAppointment(appointmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }


    @GetMapping("/findAll")
    @Operation(description = "Busca uma lista de appointments cadastrados.")
    public ResponseEntity<List<Appointment>> findAll() {
        List<Appointment> appointments = appointmentService.findAllAppointments();
        return ResponseEntity.ok(appointments);
    }


    @GetMapping("/find/{id}")
    @Operation(description = "Busca um appointment específico pelo seu ID.")
    public ResponseEntity<Appointment> findById(@PathVariable Long id) {
        Appointment appointment = appointmentService.findAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um appointment com base no seu ID.")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping("/update/{id}")
    @Operation(description = "Atualiza o appointment no banco de dados.")
    public ResponseEntity<?> updateAppointment(@Valid @PathVariable Long id, AppointmentUpdateDto appointmentUpdateDto) {
        appointmentService.updateAppointment(id, appointmentUpdateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

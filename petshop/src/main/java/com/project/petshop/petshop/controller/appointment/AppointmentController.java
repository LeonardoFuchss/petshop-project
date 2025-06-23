package com.project.petshop.petshop.controller.appointment;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.domain.entities.Appointment;
import com.project.petshop.petshop.service.interfaces.appointment.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment criado com sucesso."),
            @ApiResponse(responseCode = "409", description = "Erro em caso de conflito."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<Appointment> save (@Valid @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.createAppointment(appointmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }


    @GetMapping("/findAll")
    @Operation(description = "Busca uma lista de appointments cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista de appointments cadastrados."),
            @ApiResponse(responseCode = "404", description = "Erro em caso de nenhum registro encontrado."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso")
    })
    public ResponseEntity<List<Appointment>> findAll() {
        List<Appointment> appointments = appointmentService.findAllAppointments();
        return ResponseEntity.ok(appointments);
    }


    @GetMapping("/find/{id}")
    @Operation(description = "Busca um appointment específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o appointment encontrado."),
            @ApiResponse(responseCode = "404", description = "Erro caso o appointment informado não tenha registro no banco de dados."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Appointment> findById(@PathVariable Long id) {
        Appointment appointment = appointmentService.findAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Deleta um appointment com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem retorno. Porém, a requisição foi bem sucedida!"),
            @ApiResponse(responseCode = "404", description = "Erro caso o appointment informado não exista."),
            @ApiResponse(responseCode = "403", description = "Erro de permissão de acesso.")
    })
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

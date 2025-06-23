package com.project.petshop.petshop.service.interfaces.appointment;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.domain.entities.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(AppointmentDto appointmentDto);
    List<Appointment> findAllAppointments();
    Appointment updateAppointment(Long id, AppointmentDto appointmentDto);
    Appointment findAppointmentById(Long id);
    void deleteAppointmentById(Long id);
}

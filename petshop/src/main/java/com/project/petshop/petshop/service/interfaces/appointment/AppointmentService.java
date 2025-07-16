package com.project.petshop.petshop.service.interfaces.appointment;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.dto.AppointmentUpdateDto;

import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(AppointmentDto appointmentDto);
    List<Appointment> findAllAppointments();
    Appointment updateAppointment(Long id, AppointmentUpdateDto appointmentUpdateDto);
    Appointment findAppointmentById(Long id);
    void deleteAppointmentById(Long id);
}

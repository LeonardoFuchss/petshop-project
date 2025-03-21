package com.project.petshop.petshop.service.interfaces;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.domain.entities.Appointment;

import java.util.List;

public interface AppointmentService {

    public void save(AppointmentDto appointmentDto);
    public List<Appointment> findAll();
    public Appointment findById(Long id);
    public void delete(Long id);
}

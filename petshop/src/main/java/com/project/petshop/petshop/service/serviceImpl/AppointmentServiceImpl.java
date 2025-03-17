package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.mapper.AppointmentMapper;
import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.repository.AppointmentRepository;
import com.project.petshop.petshop.service.interfaces.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public void save(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDto);
        appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment findById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        Appointment appointment = findById(id);
        appointmentRepository.delete(appointment);
    }
}

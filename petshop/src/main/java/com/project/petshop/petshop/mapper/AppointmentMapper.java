package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.model.entities.Pets;
import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetsRepository petsRepository;

    public Appointment toEntity(AppointmentDto serviceDto) {

        Pets pets = petsRepository.findById(serviceDto.getIdPet()).orElse(null);

        return Appointment.builder()
                .pet(pets)
                .price(serviceDto.getPrice())
                .description(serviceDto.getDescription())
                .date(serviceDto.getDate())
                .petName(pets.getDogName())
                .clientName(pets.getClientName())
                .build();
    }
}

package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
import com.project.petshop.petshop.model.entities.Pets;
import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppointmentMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetsRepository petsRepository;

    public Appointment toEntity(AppointmentDto serviceDto) {

        Optional<Pets> pets = petsRepository.findById(serviceDto.getIdPet());

        if (pets.isEmpty()) {
            throw new PetsNotFound("Pet not found");
        } else {
            return Appointment.builder()
                    .pet(pets.get())
                    .price(serviceDto.getPrice())
                    .description(serviceDto.getDescription())
                    .date(serviceDto.getDate())
                    .petName(pets.get().getDogName())
                    .clientName(pets.get().getClientName())
                    .build();
        }
    }
}

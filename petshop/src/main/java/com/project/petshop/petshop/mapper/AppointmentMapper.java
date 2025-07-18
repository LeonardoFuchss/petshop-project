package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
import com.project.petshop.petshop.model.entities.Pet;
import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.model.entities.ServiceProvided;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.repository.ServiceProvidedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AppointmentMapper {
    private final PetsRepository petsRepository;
    private final ServiceProvidedRepository serviceProvidedRepository;

    public Appointment toEntity(AppointmentDto appointmentDto) {

        Optional<Pet> petFound = petsRepository.findById(appointmentDto.getPetId());
        Optional<ServiceProvided> serviceFound = serviceProvidedRepository.findById(appointmentDto.getServiceProvidedId());
        return petFound.map(pet -> Appointment.builder()
                .pet(pet)
                .price(serviceFound.get().getPrice())
                .description(serviceFound.get().getDescription())
                .date(appointmentDto.getDate())
                .build()).orElse(null);
    }
}

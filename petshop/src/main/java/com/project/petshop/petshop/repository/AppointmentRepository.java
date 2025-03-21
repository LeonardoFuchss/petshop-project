package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.domain.entities.Appointment;
import com.project.petshop.petshop.domain.entities.Pets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    public Appointment findByDate(LocalDateTime date);
    public Appointment findByPet(Pets pet);
}

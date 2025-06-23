package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.domain.entities.Appointment;
import com.project.petshop.petshop.domain.entities.Pets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByDate(LocalDateTime date);
    Appointment findByPet(Pets pet);
}

package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.model.entities.Pets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    public Appointment findByDate(LocalDateTime date);
    public Appointment findByPet(Pets pet);
}

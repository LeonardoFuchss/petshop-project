package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.model.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByDate(LocalDateTime date);
    @Query("SELECT COUNT(a) from Appointment a where a.date = :date and (:id IS NULL OR a.id <> :id)")
    long countAppointmentsByDate(@Param("date") LocalDateTime dateTime, @Param("id") int id); /* Busca no banco a quantidade de agendamentos existentes, ignorando o agendamento de id atual. */
}

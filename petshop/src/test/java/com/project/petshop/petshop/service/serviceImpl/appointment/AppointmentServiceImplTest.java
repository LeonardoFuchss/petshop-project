package com.project.petshop.petshop.service.serviceImpl.appointment;

import com.project.petshop.petshop.domain.entities.Appointment;
import com.project.petshop.petshop.domain.entities.Breed;
import com.project.petshop.petshop.domain.entities.Pets;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.domain.enums.Profile;
import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.mapper.AppointmentMapper;
import com.project.petshop.petshop.repository.AppointmentRepository;
import com.project.petshop.petshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Nested
    class SaveAppointmentTest {
        @Test
        @DisplayName("Appointment created successfully")
        void shouldSaveAppointmentWithSuccess() {
            User user = new User(1L, "05416199008", Profile.ADMIN, "Leonardo Dos Santos Fuchs", "testeSenha10", LocalDateTime.now());
            Pets pets = new Pets(1L, user, new Breed(1L, "yorkshire"), LocalDate.now(), "Leonardo Dos Santos Fuchs", "Beni", "yorkshire");
            Appointment appointment = new Appointment(1, pets, pets.getClientName(), pets.getDogName(), "Banho e tosa", 50.0, LocalDateTime.now());
            AppointmentDto appointmentDto = new AppointmentDto(1L, "Banho e tosa", 50.0, LocalDateTime.now());

            UserDetails userDetails = mock(UserDetails.class);
            Authentication authentication = mock(Authentication.class);
            SecurityContext securityContext = mock(SecurityContext.class);

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUsername()).thenReturn("05416199008");

            SecurityContextHolder.setContext(securityContext);

            doReturn(appointment).when(appointmentRepository).save(any());
            when(appointmentMapper.toEntity(appointmentDto)).thenReturn(appointment);
            when((appointmentRepository.findByDate(appointment.getDate()))).thenReturn(Optional.empty());
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(appointmentRepository.save(appointment)).thenReturn(appointment);

            Appointment savedAppointment = appointmentService.save(appointmentDto);

            assertEquals("Banho e tosa", savedAppointment.getDescription());
            assertNotNull(savedAppointment);

        }
    }

}
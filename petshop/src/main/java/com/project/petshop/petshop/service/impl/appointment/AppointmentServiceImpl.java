package com.project.petshop.petshop.service.impl.appointment;

import com.project.petshop.petshop.model.entities.Pet;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.dto.AppointmentUpdateDto;
import com.project.petshop.petshop.exceptions.appointment.AppointmentExist;
import com.project.petshop.petshop.exceptions.appointment.AppointmentNotFound;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.mapper.AppointmentMapper;
import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.repository.AppointmentRepository;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.appointment.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;
    private final PetsRepository petsRepository;


    @Override
    public Appointment createAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDto); /* Mapeamento de DTO para entidade */
        UserDetails userDetails = getUserAuth(); /* Busca usuário autenticado no contexto do spring. */
        User user = userRepository.findById(appointment.getPet().getClient().getId()).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        isAdminOrOwner(userDetails, user); /* Verifica se é admin ou owner */
        checksEqualDates(appointment); /* Verifica data do agendamento */
        return appointmentRepository.save(appointment);
    }


    @Override
    public List<Appointment> findAllAppointments() { /* Busca uma lista de agendamentos */
        List<Appointment> appointments = appointmentRepository.findAll();
        UserDetails userDetails = getUserAuth();
        if (appointments.isEmpty()) { /* Verifica se a lista retornou vazia. */
            throw new AppointmentNotFound("Appointments not found");
        }
        isAdmin(userDetails);
        return appointments;
    }

    @Override
    public Appointment updateAppointment(Long id, AppointmentUpdateDto appointmentUpdateDto) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFound("Appointment not found."));
        Pet pet = petsRepository.findById(appointment.getPet().getId()).orElseThrow(() -> new PetsNotFound("Pet not found"));
        User user = userRepository.findById(pet.getClient().getId()).orElseThrow(() -> new UserNotFoundException("User not found."));
        UserDetails userDetails = getUserAuth();
        isAdminOrOwner(userDetails, user);
        appointment.setDate(appointmentUpdateDto.getDate());
        checksEqualDates(appointment);
        appointment.setPrice(appointmentUpdateDto.getPrice());
        return appointmentRepository.save(appointment);
    }


    @Override
    public Appointment findAppointmentById(Long id) { /* Busca um agendamento com base no identificador */
        Optional<Appointment> appointmentOptional = Optional.ofNullable(appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFound("Appointment not found")));
        UserDetails userDetails = getUserAuth();
        Optional<Pet> petFind = petsRepository.findById(appointmentOptional.get().getPet().getId());
        User user = userRepository.findById(petFind.get().getClient().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        isAdminOrOwner(userDetails, user);
        return appointmentOptional.get();
    }


    @Override
    public void deleteAppointmentById(Long id) { /* Deleta um agendamento com base no seu ID */
        Appointment appointment = findAppointmentById(id);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(appointment.getPet().getClient().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        isAdminOrOwner(userDetails, user);
        appointmentRepository.delete(appointment);
    }




    /**
     ****************************************************************************************************************************************************
     */





    /* MÉTODOS REUTILIZÁVEIS */

    private void checksEqualDates(Appointment appointment) { /* Verifica se o número de agendamentos nesta data é maior ou igual a 7 */
        long totalAppointmentByDate = appointmentRepository.countAppointmentsByDate(appointment.getDate(), appointment.getId());
        if (totalAppointmentByDate >= 7) {
            throw new AppointmentExist("This date is not available");
        }
    }
    private UserDetails getUserAuth() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    private void isAdmin(UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new UnauthorizedException("You do not have permission.");
        }
    }
    private void isAuthorized(UserDetails userDetails, User user) {
        boolean isAuthorized = userDetails.getUsername().equals(user.getUserCpf());
        if (!isAuthorized) {
            throw new UnauthorizedException("You do not have permission");
        }
    }
    private void isAdminOrOwner(UserDetails userDetails, User user) {
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isAuthorized = userDetails.getUsername().equals(user.getUserCpf());
        if (!isAdmin && !isAuthorized) {
            throw new UnauthorizedException("You do not have permission.");
        }
    }
}
/**
 * Princípios seguidos:
 * SRP: Single Responsibility Principle
 * DRY: Don´t Repeat Yourself
 * Segurança com base em roles
 * Clareza de código
 */
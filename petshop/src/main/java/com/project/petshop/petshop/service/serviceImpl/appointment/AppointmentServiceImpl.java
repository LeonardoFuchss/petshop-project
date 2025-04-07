package com.project.petshop.petshop.service.serviceImpl.appointment;

import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.exceptions.appointment.AppointmentExist;
import com.project.petshop.petshop.exceptions.appointment.AppointmentNotFound;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.mapper.AppointmentMapper;
import com.project.petshop.petshop.domain.entities.Appointment;
import com.project.petshop.petshop.repository.AppointmentRepository;
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

    @Override
    public Appointment save(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDto); /* Mapeamento de DTO para entidade */
        Optional<Appointment> appointmentExist = appointmentRepository.findByDate(appointmentDto.getDate()); /* Busca se a data já possui um agendamento */
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); /* Busca usuário autenticado no contexto do spring. */
        User user = userRepository.findById(appointment.getPet().getClient().getId()).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        if (appointmentExist.isPresent()) {
            throw new AppointmentExist("The Appointment already exists.");
        }
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isAuthorized = userDetails.getUsername().equals(user.getUserCpf());
        if (!isAdmin && !isAuthorized) {
            throw new UnauthorizedException("You do not have permission to add an appointment.");
        }
        return appointmentRepository.save(appointment);
    }


    @Override
    public List<Appointment> findAll() { /* Busca uma lista de agendamentos */
        List<Appointment> appointments = appointmentRepository.findAll();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appointments.isEmpty()) { /* Verifica se a lista retornou vazia. */
            throw new AppointmentNotFound("Appointments not found");
        }
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) { /* Se o usuário for admin, libera a visualização dos usuários */
            return appointments;
        } else {
            throw new UnauthorizedException("You do not have permission to view all appointments");
        }
    }


    @Override
    public Appointment findById(Long id) { /* Busca um agendamento com base no identificador */
        Optional<Appointment> appointmentOptional = Optional.ofNullable(appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFound("Appointment not found")));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isAuthorized = userDetails.getUsername().equals(appointmentOptional.get().getClientName());

        if (!isAdmin && !isAuthorized) {
            throw new UnauthorizedException("You do not have permission to view an appointment.");
        }
        return appointmentOptional.get();
    }


    @Override
    public void delete(Long id) { /* Deleta um agendamento com base no seu ID */
        Appointment appointment = findById(id);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = Optional.ofNullable(userRepository.findById(appointment.getPet().getClient().getId()).orElseThrow(() -> new UserNotFoundException("User not found")));
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isAuthorized = userDetails.getUsername().equals(appointment.getClientName());

        if (!isAdmin && !isAuthorized) {
            throw new UnauthorizedException("You do not have permission to delete an appointment.");
        }
        appointmentRepository.delete(appointment);
    }
}

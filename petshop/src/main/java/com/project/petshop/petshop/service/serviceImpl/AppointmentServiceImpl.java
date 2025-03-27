package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.exceptions.appointment.AppointmentExist;
import com.project.petshop.petshop.exceptions.appointment.AppointmentNotFound;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.mapper.AppointmentMapper;
import com.project.petshop.petshop.domain.entities.Appointment;
import com.project.petshop.petshop.repository.AppointmentRepository;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Appointment save(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDto); /* Mapeamento de DTO para entidade */
        Appointment appointmentExist = appointmentRepository.findByDate(appointmentDto.getDate()); /* Busca se a data já possui um agendamento */
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); /* Busca usuário autenticado no contexto do spring. */
        Optional<User> user = userRepository.findById(appointment.getPet().getClient().getId());

        if (appointmentExist != null) { /* Se já existe um agendamento para essa data, lança a exceção */
            throw new AppointmentExist("Appointment already exist in this date");
        }
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) { /* Se o role do usuário autenticado for "ADMIN", permite salvar no banco */
            return appointmentRepository.save(appointment);
        } else if (userDetails.getUsername().equals(user.get().getUserCpf())) { /* Se o nome do usuário autenticado (cpf) for igual ao cliente dono do pet, permite salvar */
            return appointmentRepository.save(appointment);
        } else { /* Caso o contrário, lança exceção de permissão */
            throw new UnauthorizedException("You do not have permission to add an appointment");
        }
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
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (appointmentOptional.isEmpty()) {
            throw new AppointmentNotFound("Appointments not found");
        }

        /* Lógica para permitir que apenas o admin possa visualizar qualquer agendamento e o cliente possa visualizar apenas o seu. */
        if (userDetails.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"))) {
            return appointmentOptional.get();
        }
        if (userDetails.getUsername().equals(appointmentOptional.get().getClientName())) {
            return appointmentOptional.get();
        } else {
            throw new UnauthorizedException("You do not have permission to found this appointment");
        }
    }

    @Override
    public void delete(Long id) { /* Deleta um agendamento com base no seu ID */
        Appointment appointment = findById(id);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findById(appointment.getPet().getClient().getId());

        if (userDetails.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"))) {
            appointmentRepository.delete(appointment);
        } else if (userDetails.getUsername().equals(user.get().getUserCpf())) {
            appointmentRepository.delete(appointment);
        } else {
            throw new UnauthorizedException("You do not have permission to delete this appointment");
        }
    }
}

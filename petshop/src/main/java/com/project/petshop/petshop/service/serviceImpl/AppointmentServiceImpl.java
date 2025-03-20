package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.AppointmentDto;
import com.project.petshop.petshop.exceptions.address.AddressNotFound;
import com.project.petshop.petshop.exceptions.appointment.AppointmentExist;
import com.project.petshop.petshop.exceptions.appointment.AppointmentNotFound;
import com.project.petshop.petshop.mapper.AppointmentMapper;
import com.project.petshop.petshop.model.entities.Appointment;
import com.project.petshop.petshop.repository.AppointmentRepository;
import com.project.petshop.petshop.service.interfaces.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public void save(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDto); /* Mapeamento de DTO para entidade */
        Appointment petExist = appointmentRepository.findByPet(appointment.getPet()); /* Busca se o pet já possui um agendamento registrado */

        if (petExist == null) { /* Se o pet não possuir um agendamento */
            Appointment appointmentDate = appointmentRepository.findByDate(appointment.getDate()); /* Busca se a data definida para o novo agendamento já existe */
            if (appointmentDate == null) { /* Se a data não existir (estiver disponível) */
                appointmentRepository.save(appointment); /* Persiste um novo agendamento no banco de dados. */
            } else {
                throw new AppointmentExist("Appointment in this date already exist");
            }
        } else { /* Se o pet já possuir um agendamento registrado */
            Appointment appointmentDate = appointmentRepository.findByDate(appointment.getDate()); /* Verifica se a data definida para o novo agendamento já existe. */
            if (appointmentDate != null) { /* Verifica se a data já existe. */
                throw new AppointmentExist("Appointment already exist");
            }
            appointmentRepository.save(appointment); /* Se não existir, salva um novo agendamento para o pet existente. */
        }
    }

    @Override
    public List<Appointment> findAll() { /* Busca uma lista de agendamentos */
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) { /* Verifica se a lista retornou vazia. */
            throw new AppointmentNotFound("Appointments not found");
        }
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment findById(Long id) { /* Busca um agendamento com base no identificador */
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isEmpty()) { /* Verifica se a consulta retornou vazia. */
            throw new AppointmentNotFound("Appointment not found");
        }
        return appointmentOptional.get();
    }

    @Override
    public void delete(Long id) { /* Deleta um agendamento com base no seu ID */
        Appointment appointment = findById(id);
        if (appointment == null) { /* Verifica se o agendamento existe */
            throw new AppointmentNotFound("Appointment not found");
        }
        appointmentRepository.delete(appointment); /* Deleta um agendamento do banco de dados. */
    }
}

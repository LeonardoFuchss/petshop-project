package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.exceptions.pets.PetsAlreadyExist;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
import com.project.petshop.petshop.mapper.PetsMapper;
import com.project.petshop.petshop.model.entities.Pets;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.PetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetsServiceImpl implements PetsService {

    @Autowired
    private PetsRepository petsRepository;
    @Autowired
    private PetsMapper petsMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Pets save(PetsDto petsDto) {
        /* Lógica: Verifica se já existe um cliente e um pet com os mesmos nomes. */
        Pets pets = petsMapper.toEntity(petsDto);

        List<Pets> petClientFind = petsRepository.findByClientName(pets.getClientName());
        List<Pets> pet = petsRepository.findByDogName(pets.getDogName());

        if (petClientFind.isEmpty() && pet.isEmpty()) {
            petsRepository.save(pets);
        } else if (!petClientFind.isEmpty() && pet.isEmpty()) {
            petsRepository.save(pets);
        } else {
            throw new PetsAlreadyExist("Pets already exist");
        }
        return pets;
    }

    @Override
    public List<Pets> findAll() {
        List<Pets> petsList = petsRepository.findAll();
        if (petsList.isEmpty()) {
            throw new PetsNotFound("No pet record was found.");
        }
        return petsRepository.findAll();
    }

    @Override
    public List<Pets> findByClientName(String clientName) {
        List<Pets> pet = petsRepository.findByClientName(clientName);
        if (pet.isEmpty()) {
            throw new PetsNotFound("No pet record was found.");
        }
        return pet;
    }

    @Override
    public Optional<Pets> findById(Long id) {
        Optional<Pets> pets = petsRepository.findById(id);
        if (pets.isEmpty()) {
            throw new PetsNotFound("No pet record was found.");
        }
        return pets;
    }

    @Override
    public void delete(Long id) {
        Optional<Pets> pets = petsRepository.findById(id);
        if (pets.isEmpty()) {
            throw new PetsNotFound("No pet record was found.");
        }
        pets.ifPresent(value -> petsRepository.delete(value));
    }
}

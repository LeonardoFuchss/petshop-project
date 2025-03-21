package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.exceptions.pets.PetsAlreadyExist;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
import com.project.petshop.petshop.mapper.PetsMapper;
import com.project.petshop.petshop.domain.entities.Pets;
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

        Pets pets = petsMapper.toEntity(petsDto); /* Mapeamento de DTO para entidade */

        List<Pets> petClientFind = petsRepository.findByClientName(pets.getClientName()); /* Busca cliente pelo nome */
        List<Pets> pet = petsRepository.findByDogName(pets.getDogName()); /* Busca pet pelo nome */

        if (petClientFind.isEmpty() && pet.isEmpty()) { /* Se não existir um usuário e nem um pet salvo, persiste um novo pet no banco de dados */
            petsRepository.save(pets);
        } else if (!petClientFind.isEmpty() && pet.isEmpty()) { /* Se o usuário existir, mas o pet ainda não, persiste um novo pet no banco de dados. */
            petsRepository.save(pets);
        } else {
            throw new PetsAlreadyExist("Pets already exist"); /* Se o usuário existir e o pet também, lança a exceção de conflito. */
        }
        return pets;
    }

    @Override
    public List<Pets> findAll() {
        List<Pets> petsList = petsRepository.findAll(); /* Busca uma lista de pets */
        if (petsList.isEmpty()) { /* Verifica se a lista retornou vazia */
            throw new PetsNotFound("No pet record was found.");
        }
        return petsRepository.findAll();
    }

    @Override
    public List<Pets> findByClientName(String clientName) { /* Busca pet pelo nome do usuário */
        List<Pets> pet = petsRepository.findByClientName(clientName);
        if (pet.isEmpty()) { /* Verifica se a consulta retornou vazia */
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
    public void delete(Long id) { /* Deleta um pet com base no seu identificador */
        Optional<Pets> pets = petsRepository.findById(id); /* Busca um pet com base no seu identificador */
        if (pets.isEmpty()) { /* verifica se a consulta retornou vazia */
            throw new PetsNotFound("No pet record was found.");
        }
        pets.ifPresent(value -> petsRepository.delete(value)); /* Se o pet existir, deleta. */
    }
}

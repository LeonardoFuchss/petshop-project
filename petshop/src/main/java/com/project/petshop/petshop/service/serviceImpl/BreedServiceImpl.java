package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.exceptions.breed.BreedAlreadyExist;
import com.project.petshop.petshop.exceptions.breed.BreedNotFound;
import com.project.petshop.petshop.mapper.BreedMapper;
import com.project.petshop.petshop.domain.entities.Breed;
import com.project.petshop.petshop.repository.BreedRepository;
import com.project.petshop.petshop.service.interfaces.BreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BreedServiceImpl implements BreedService {

    @Autowired
    private BreedRepository breedRepository;
    @Autowired
    private BreedMapper breedMapper;

    @Override
    public Breed save(BreedDto breedDto) {
        Breed breed = breedMapper.toEntity(breedDto); /* Mapeamento de DTO para entidade */
        Breed breedFind = breedRepository.findByDescription(breed.getDescription()); /* Busca uma raça com base na sua descrição */
        if (breedFind != null) { /* Verifica se existe uma raça com a mesma descrição */
            throw new BreedAlreadyExist("Breed already exist");
        }
        breedRepository.save(breed); /* Persiste no banco de dados */
        return breed;
    }

    @Override
    public List<Breed> findAll() { /* Busca uma lista de raças */
        List<Breed> breeds = breedRepository.findAll();
        if (breeds.isEmpty()) { /* Verifica se a lista está vazia */
            throw new BreedNotFound("Breeds not found");
        }
        return breedRepository.findAll();
    }

    @Override
    public Optional<Breed> findById(Long id) { /* Busca uma raça com base no seu identificador */
        Optional<Breed> breed = breedRepository.findById(id);
        if (breed.isEmpty()) { /* Verifica se a consulta retornou vazia. */
            throw new BreedNotFound("Breeds not found");
        }
        return breedRepository.findById(id);
    }

    @Override
    public void delete(Long id) { /* Deleta uma raça com base no seu identificador */
        Optional<Breed> breedFind = breedRepository.findById(id); /* Busca uma raça com base no ID */
        if (breedFind.isPresent()) { /* Verifica se a consulta retornou vazia. */
            breedRepository.delete(breedFind.get()); /* Deleta a raça do banco de dados */
        } else {
            throw new BreedNotFound("Breed not found");
        }
    }
}

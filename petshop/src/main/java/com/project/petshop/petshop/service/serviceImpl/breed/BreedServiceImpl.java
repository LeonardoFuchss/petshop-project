package com.project.petshop.petshop.service.serviceImpl.breed;

import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.exceptions.breed.BreedAlreadyExist;
import com.project.petshop.petshop.exceptions.breed.BreedNotFound;
import com.project.petshop.petshop.mapper.BreedMapper;
import com.project.petshop.petshop.domain.entities.Breed;
import com.project.petshop.petshop.repository.BreedRepository;
import com.project.petshop.petshop.service.interfaces.breeds.BreedService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BreedServiceImpl implements BreedService {

    private final BreedRepository breedRepository;
    private final BreedMapper breedMapper;

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
        return breeds;
    }



    @Override
    public Optional<Breed> findById(Long id) { /* Busca uma raça com base no seu identificador */
        return Optional.ofNullable(breedRepository.findById(id).orElseThrow(() -> new BreedNotFound("Breed not found")));
    }


    @Override
    public void delete(Long id) { /* Deleta uma raça com base no seu identificador */
        Breed breedFind = (breedRepository.findById(id).orElseThrow(() -> new BreedNotFound("Breed not found"))); /* Busca uma raça com base no ID */
        breedRepository.delete(breedFind); /* Deleta a raça do banco de dados */

    }
}

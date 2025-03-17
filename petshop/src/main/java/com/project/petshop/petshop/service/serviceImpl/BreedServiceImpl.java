package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.mapper.BreedMapper;
import com.project.petshop.petshop.model.entities.Breed;
import com.project.petshop.petshop.repository.BreedRepository;
import com.project.petshop.petshop.service.interfaces.BreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BreedServiceImpl implements BreedService {

    @Autowired
    private BreedRepository breedRepository;
    @Autowired
    private BreedMapper breedMapper;

    @Override
    public void save(BreedDto breedDto) {
        Breed breed = breedMapper.toEntity(breedDto);
        breedRepository.save(breed);
    }

    @Override
    public List<Breed> findAll() {
        return breedRepository.findAll();
    }

    @Override
    public Breed findById(Long id) {
        return breedRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        Breed breed = breedRepository.findById(id).orElse(null);
        breedRepository.delete(breed);
    }
}

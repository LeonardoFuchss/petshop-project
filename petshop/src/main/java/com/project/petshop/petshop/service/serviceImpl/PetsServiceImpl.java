package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.mapper.PetsMapper;
import com.project.petshop.petshop.model.entities.Pets;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.service.interfaces.PetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PetsServiceImpl implements PetsService {

    @Autowired
    private PetsRepository petsRepository;
    @Autowired
    private PetsMapper petsMapper;

    @Override
    public void save(PetsDto petsDto) {
        Pets pets = petsMapper.toEntity(petsDto);
        petsRepository.save(pets);
    }

    @Override
    public List<Pets> findAll() {
        return petsRepository.findAll();
    }

    @Override
    public List<Pets> findByClientName(String clientName) {
        return petsRepository.findByClientName(clientName);
    }

    @Override
    public Pets findById(Long id) {
        return petsRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        Pets pets = petsRepository.findById(id).orElse(null);
        petsRepository.delete(pets);
    }
}

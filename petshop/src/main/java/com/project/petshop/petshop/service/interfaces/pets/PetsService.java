package com.project.petshop.petshop.service.interfaces.pets;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.domain.entities.Pets;

import java.util.List;
import java.util.Optional;

public interface PetsService {
    public Pets save(PetsDto petsDto);
    public List<Pets> findAll();
    public Pets findByClientName(String clientName);
    public Pets findById(Long id);
    public void delete(Long id);
    public Pets update(PetsDto petsDto);
}

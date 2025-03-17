package com.project.petshop.petshop.service.interfaces;

import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.model.entities.Pets;

import java.util.List;

public interface PetsService {

    public void save(PetsDto petsDto);
    public List<Pets> findAll();
    public List<Pets> findByClientName(String clientName);
    public Pets findById(Long id);
    public void delete(Long id);
}

package com.project.petshop.petshop.service.interfaces.address;


import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.domain.entities.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    public Address save(AddressDto addressDto);
    public Optional<Address> findById(Long id);
    public List<Address> findAll();
    public void deleteById(Long id);

}

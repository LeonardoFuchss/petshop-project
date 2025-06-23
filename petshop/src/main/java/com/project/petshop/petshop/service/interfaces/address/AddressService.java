package com.project.petshop.petshop.service.interfaces.address;


import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.domain.entities.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    Address createAddress(AddressDto addressDto);
    Optional<Address> findAddressById(Long id);
    List<Address> findAllAddress();
    void deleteAddressById(Long id);
}

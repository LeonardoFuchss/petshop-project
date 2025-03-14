package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.mapper.AddressMapper;
import com.project.petshop.petshop.model.entities.Address;
import com.project.petshop.petshop.repository.AddressRepository;
import com.project.petshop.petshop.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressMapper addressMapper;


    @Override
    public void save(AddressDto addressDto) {
        addressRepository.save(addressMapper.toEntity(addressDto));
    }

    @Override
    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        address.ifPresent(value -> addressRepository.delete(value));
    }
}

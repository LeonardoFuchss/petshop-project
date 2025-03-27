package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.exceptions.address.AddressAlreadyExist;
import com.project.petshop.petshop.exceptions.address.AddressNotFound;
import com.project.petshop.petshop.mapper.AddressMapper;
import com.project.petshop.petshop.domain.entities.Address;
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
    public Address save(AddressDto addressDto) { /* Registra um novo endereço no banco de dados */
        Address address = addressMapper.toEntity(addressDto); /* Mapeamento de DTO para entidade */
        Optional<Address> addressFindByUser = addressRepository.findByUser(address.getUser()); /* Busca um endereço com base no usuário */

        if (addressFindByUser.isPresent()) { /* Verifica se o endereço já existe. */
            throw new AddressAlreadyExist("Address already exist");
        } else {
           return addressRepository.save(address); /* Persiste um novo endereço no banco de dados */
        }
    }


    @Override
    public Optional<Address> findById(Long id) { /* Busca um usuário com base no seu identificador */
        Optional<Address> addressFindById = addressRepository.findById(id);
        if (addressFindById.isPresent()) { /* Verifica se o endereço existe */
            return addressFindById;
        } else {
            throw new AddressNotFound("Address not found");
        }
    }

    @Override
    public List<Address> findAll() { /* Busca uma lista de endereços */
        List<Address> addressFindAll = addressRepository.findAll();
        if (addressFindAll.isEmpty()) { /* Verifica se a lista esta vazia */
            throw new AddressNotFound("Address not found");
        }
        return addressRepository.findAll();
    }

    @Override
    public void deleteById(Long id) { /* Deleta o endereço com base no ID */
        Optional<Address> address = addressRepository.findById(id); /* Busca um endereço com base no ID */
        if (address.isEmpty()) { /* Verifica se o endereço existe */
            throw new AddressNotFound("Address not found");
        }
        address.ifPresent(value -> addressRepository.delete(value)); /* Se existir deleta do banco de dados */
    }
}

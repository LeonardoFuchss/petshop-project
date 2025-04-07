package com.project.petshop.petshop.service.serviceImpl.address;

import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.exceptions.address.AddressAlreadyExist;
import com.project.petshop.petshop.exceptions.address.AddressNotFound;
import com.project.petshop.petshop.mapper.AddressMapper;
import com.project.petshop.petshop.domain.entities.Address;
import com.project.petshop.petshop.repository.AddressRepository;
import com.project.petshop.petshop.service.interfaces.address.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {


    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;


    @Override
    public Address save(AddressDto addressDto) { /* Registra um novo endereço no banco de dados */
        Address address = addressMapper.toEntity(addressDto); /* Mapeamento de DTO para entidade */
        Optional<Address> addressFindByUser = addressRepository.findByUser(address.getUser()); /* Busca um endereço com base no usuário */
        if (addressFindByUser.isPresent()) { /* Verifica se o endereço já existe. */
            throw new AddressAlreadyExist("The Address informed already exists.");
        } else {
            return addressRepository.save(address); /* Persiste um novo endereço no banco de dados */
        }
    }


    @Override
    public Optional<Address> findById(Long id) { /* Busca um usuário com base no seu identificador */
        return Optional.ofNullable(
                addressRepository.findById(id).orElseThrow(() -> new AddressNotFound("The Address informed does not exist."))
        );
    }


    @Override
    public List<Address> findAll() { /* Busca uma lista de endereços */
        List<Address> addressFindAll = addressRepository.findAll();
        if (addressFindAll.isEmpty()) { /* Verifica se a lista esta vazia */
            throw new AddressNotFound("No address found.");
        }
        return addressFindAll;
    }


    @Override
    public void deleteById(Long id) { /* Deleta o endereço com base no ID */
        Address address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFound("The Address informed does not exist."));
        addressRepository.delete(address); /* Se existir deleta do banco de dados */
    }
}

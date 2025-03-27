package com.project.petshop.petshop.service.serviceImpl;

import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.exceptions.pets.PetsAlreadyExist;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.mapper.PetsMapper;
import com.project.petshop.petshop.domain.entities.Pets;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.PetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PetsServiceImpl implements PetsService {

    @Autowired
    private PetsRepository petsRepository;
    @Autowired
    private PetsMapper petsMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Pets save(PetsDto petsDto) {

        Pets pets = petsMapper.toEntity(petsDto); /* Mapeamento de DTO para entidade */

        Optional<Pets> petOfClientFind = petsRepository.findByClientName(pets.getClientName()); /* Busca pet pelo nome do cliente */
        Optional<Pets> petName = petsRepository.findByDogName(pets.getDogName()); /* Busca pet pelo nome */

        if (petName.isPresent() && petOfClientFind.isPresent()) {
            throw new PetsAlreadyExist("Pets already exist");
        } else if (petOfClientFind.isEmpty()) {
            petsRepository.save(pets);
        }
        return pets;
    }

    @Override
    public List<Pets> findAll() {
        UserDetails userAuth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Pets> petsList = petsRepository.findAll(); /* Busca uma lista de pets */

        if (petsList.isEmpty()) { /* Verifica se a lista retornou vazia */
            throw new PetsNotFound("No pet record was found.");
        }
        if (userAuth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return petsRepository.findAll();
        } else {
            throw new UnauthorizedException("You are not allowed to view all users. Search for your pet.");
        }
    }

    @Override
    public Optional<Pets> findByClientName(String clientName) { /* Busca pet pelo nome do usuário */
        Optional<Pets> pet = petsRepository.findByClientName(clientName);
        if (pet.isEmpty()) { /* Verifica se a consulta retornou vazia */
            throw new PetsNotFound("No pet record was found.");
        }
        return pet;
    }

    @Override
    public Optional<Pets> findById(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); /* Busca o usuário autenticado no contexto do spring. */
        Optional<Pets> pets = petsRepository.findById(id); /* Busca o pet pelo id informado */

        if (pets.isEmpty()) { /* Retorna erro se estiver vazio */
            throw new PetsNotFound("No pet record was found.");
        }
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) { /* Returna o usuário buscado pelo id se o role do usuário autenticado for admin */
            return pets;
        }
        Optional<User> user = userRepository.findById(pets.get().getClient().getId()); /* Busca o usuário a partir do pet encontrado */

        if (Objects.equals(userDetails.getUsername(), user.get().getUserCpf())) { /* Verifica se o usuário autenticado é igual ao usuário dono do pet. */
            return pets; /* Se for, ele poderá visualizar */
        } else {
            throw new UnauthorizedException("You do not have permission to access this resource");
        }
    }

    @Override
    public void delete(Long id) { /* Deleta um pet com base no seu identificador */
        UserDetails userAuth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Pets> pets = petsRepository.findById(id); /* Busca um pet com base no seu identificador */

        if (pets.isEmpty()) { /* verifica se a consulta retornou vazia */
            throw new PetsNotFound("No pet record was found.");
        }

        if (userAuth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            pets.ifPresent(value -> petsRepository.delete(value));
        }

        Optional<User> user = userRepository.findById(pets.get().getClient().getId());
        if (userAuth.getUsername().equals(user.get().getUserCpf())) {
            petsRepository.delete(pets.get());
        } else {
            throw new UnauthorizedException("You do not have permission to access this resource");
        }
    }
}
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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PetsServiceImpl implements PetsService {


    private final PetsRepository petsRepository;
    private final PetsMapper petsMapper;
    private final UserRepository userRepository;

    @Override
    public Pets save(PetsDto petsDto) {

        Pets pets = petsMapper.toEntity(petsDto); /* Mapeamento de DTO para entidade */
        Optional<User> userPet = userRepository.findById(pets.getClient().getId());
        Optional<Pets> petExist = petsRepository.findByClientNameAndDogName(userPet.get().getFullName(), pets.getDogName());/* Busca pet pelo nome */

        if (petExist.isPresent()) {
            throw new PetsAlreadyExist("Pets already exist");
        }
        return petsRepository.save(pets);
    }


    @Override
    public List<Pets> findAll() {
        UserDetails userAuth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Pets> petsList = petsRepository.findAll(); /* Busca uma lista de pets */
        boolean isAdmin = userAuth.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
        if (petsList.isEmpty()) { /* Verifica se a lista retornou vazia */
            throw new PetsNotFound("No pet record was found.");
        }
        if (isAdmin) {
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
        UserDetails userDetails = userAuth();
        Optional<Pets> pets = Optional.ofNullable(petsRepository.findById(id).orElseThrow(() -> new PetsNotFound("Pet not found"))); /* Busca o pet pelo id informado */
        Optional<User> user = Optional.ofNullable(userRepository.findById(pets.get().getClient().getId()).orElseThrow(() -> new PetsNotFound("User pets not found")));
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch((g) -> g.getAuthority().equals("ROLE_ADMIN"));
        boolean isAuth = userDetails.getUsername().equals(user.get().getUserCpf());

        if (!isAdmin && !isAuth) {
            throw new UnauthorizedException("You are not authorized to view all users. Search for your pet.");
        } else {
            return pets;
        }
    }


    @Override
    public void delete(Long id) { /* Deleta um pet com base no seu identificador */
        UserDetails userAuth = userAuth();
        Optional<Pets> pets = Optional.ofNullable(petsRepository.findById(id).orElseThrow(() -> new PetsNotFound("Pet not found"))); /* Busca um pet com base no seu identificador */
        Optional<User> user = Optional.ofNullable(userRepository.findById(pets.get().getClient().getId()).orElseThrow(() -> new PetsNotFound("User pets not found")));
        boolean isAdmin = userAuth.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
        boolean isAuth = userAuth.getUsername().equals(user.get().getUserCpf());
        if (!isAdmin && !isAuth) {
            throw new UnauthorizedException("You are not authorized to delete this user. Search for your pet.");
        }
        petsRepository.deleteById(pets.get().getId());
    }


    @Override
    public Pets update(PetsDto petsDto) {
        UserDetails userAuth = userAuth();
        return saveUpdate(userAuth, petsDto);
    }
    private boolean isAuthorized(UserDetails user, PetsDto petsDto) {
        Optional<User> getUser = userRepository.findById(petsDto.getIdClient());
        return user.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
                || user.getUsername().equals(getUser.get().getUserCpf());
    }
    private Pets saveUpdate(UserDetails user, PetsDto petsDto) {
        Optional<Pets> pets = petsRepository.findById(petsDto.getIdClient());
        if (isAuthorized(user, petsDto)) {
            if (pets.isPresent()) {
                pets.get().setBirthDate(petsDto.getBirthDate());
                pets.get().setDogName(petsDto.getDogName());
                return petsRepository.save(pets.get());
            } else {
                throw new PetsNotFound("No pet record was found.");
            }
        } else {
            throw new UnauthorizedException("You do not have permission to access this resource");
        }
    }

    private UserDetails userAuth(){
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    };
}
package com.project.petshop.petshop.service.impl.pets;

import com.project.petshop.petshop.dto.PetDtoPublic;
import com.project.petshop.petshop.exceptions.user.UserNotFoundException;
import com.project.petshop.petshop.mapper.PetMapperPublic;
import com.project.petshop.petshop.model.entities.Pet;
import com.project.petshop.petshop.model.entities.User;
import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.exceptions.pets.PetsAlreadyExist;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.mapper.PetsMapper;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.pets.PetsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final PetMapperPublic petMapperPublic;


    @Override
    public Pet createPet(PetsDto petsDto) {
        Pet pet = petsMapper.toEntity(petsDto);
        Optional<User> user = userRepository.findById(pet.getClient().getId());
        UserDetails userAuth = getUserAuth();
        userAuthOrIsAdmin(userAuth, user.get().getUserCpf());
        return petsRepository.save(pet);
    }

    @Override
    public Pet createPetPublic(PetDtoPublic petDtoPublic) {
        Pet pet = petMapperPublic.toEntity(petDtoPublic);
        UserDetails auth = getUserAuth();
        Optional<User> userFound = Optional.ofNullable(userRepository.findByUserCpf(auth.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found.")));
        pet.setClient(userFound.get());
        return petsRepository.save(pet);
    }

    @Override
    public List<Pet> findAllPets() {
        UserDetails userAuth = getUserAuth();
        isAdmin(userAuth);
        List<Pet> petList = petsRepository.findAll();
        petsListIsEmpty(petList);
        return petList;
    }

    @Override
    public Pet findPetById(Long id) {
        UserDetails userDetails = getUserAuth();
        Pet pet = petsRepository.findById(id).orElseThrow(() -> new PetsNotFound("No pet record was found."));
        User user = userRepository.findById(pet.getClient().getId()).orElseThrow(() -> new PetsNotFound("No user record was found."));
        userAuthOrIsAdmin(userDetails, user.getUserCpf());
        return pet;
    }

    @Override
    public void deletePetById(Long id) {
        UserDetails userAuth = getUserAuth();
        Pet pet = petsRepository.findById(id).orElseThrow(() -> new PetsNotFound("Pet not found"));
        User user = userRepository.findById(pet.getClient().getId()).orElseThrow(() -> new PetsNotFound("User pets not found"));
        userAuthOrIsAdmin(userAuth, user.getUserCpf());
        petsRepository.deleteById(pet.getId());
    }


    @Override
    public Pet updatePet(PetsDto petsDto) {
        UserDetails userAuth = getUserAuth();
        return saveUpdate(userAuth, petsDto);
    }


    private Pet saveUpdate(UserDetails userAuth, PetsDto petsDto) {
        User user = userRepository.findById(petsDto.getUserId()).orElseThrow(() -> new PetsNotFound("No user record was found."));
        userAuthOrIsAdmin(userAuth, user.getUserCpf());
        Pet pet = petsMapper.toEntity(petsDto);
        pet.setBirthDate(petsDto.getBirthDate());
        pet.setDogName(petsDto.getDogName());
        return petsRepository.save(pet);
    }


    /**
     *********************************************************************************************************************************
     */

    // Métodos reutilizáveis


    private void isAdmin(UserDetails userAuth) {
        boolean isAdmin = userAuth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new UnauthorizedException("You do not have permission to access this resource");
        }
    }

    private void userAuthOrIsAdmin(UserDetails userDetails, String cpf) {
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !Objects.equals(userDetails.getUsername(), cpf)) {
            throw new UnauthorizedException("You are not allowed to access this resource. Please login with admin account or contact the admin to register you as an admin account.");
        }
    }
    private UserDetails getUserAuth() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void petsListIsEmpty(List<Pet> pets) {
        if (pets == null) {
            throw new PetsNotFound("No pets found.");
        }
    }
}
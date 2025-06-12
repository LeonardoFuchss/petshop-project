package com.project.petshop.petshop.service.serviceImpl.pets;

import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.exceptions.pets.PetsAlreadyExist;
import com.project.petshop.petshop.exceptions.pets.PetsNotFound;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.mapper.PetsMapper;
import com.project.petshop.petshop.domain.entities.Pets;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.repository.UserRepository;
import com.project.petshop.petshop.service.interfaces.pets.PetsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PetsServiceImpl implements PetsService {

    private final PetsRepository petsRepository;
    private final PetsMapper petsMapper;
    private final UserRepository userRepository;


    /**
     * Persiste um novo pet no banco de dados.
     * .
     * Recebe o DTO por parâmetro, faz o mapeamento de DTO para entidade.
     * Verifica se o Usuário do pet existe, se existir verifica se ele já possui um pet com esse nome cadastrado.
     * Salva um novo peto no banco de dados se ainda não existir.
     */
    @Override
    public Pets save(PetsDto petsDto) {
        Pets pets = petsMapper.toEntity(petsDto);
        userPetIsPresent(pets);
        return petsRepository.save(pets);
    }

    /**
     * Realiza uma busca de todos os pets registrados no banco de dados.
     * .
     * Recupera o usuário autenticado e verifica se ele é admin (apenas admin devem ter acesso).
     * Recupera a lista dos pets.
     */
    @Override
    public List<Pets> findAll() {
        UserDetails userAuth = getUserAuth();
        isAdmin(userAuth);
        List<Pets> petsList = petsRepository.findAll();
        petsListIsEmpty(petsList);
        return petsList;
    }

    /**
     * Busca um pet pelo nome do seu usuário.
     */
    @Override
    public Pets findByClientName(String clientName) {
        return petsRepository.findByClientName(clientName).orElseThrow(() -> new PetsNotFound("No pet record was found."));
    }


    /**
     * Retorna um usuário com base no id passado por parâmetro.
     * Recupera o usuário autenticado.
     * Recupera o pet e o seu usuário pelo ID.
     * verifica se o usuário autenticado é admin ou se o usuário do pet é igual ao usuário autenticado.
     * (Verificação feita para apenas admins conseguirem visualizar ou usuários client visualizarem apenas o registro de seus pets).
     */
    @Override
    public Pets findById(Long id) {
        UserDetails userDetails = getUserAuth();
        Pets pets = petsRepository.findById(id).orElseThrow(() -> new PetsNotFound("No pet record was found."));
        User user = userRepository.findById(pets.getClient().getId()).orElseThrow(() -> new PetsNotFound("No user record was found."));
        userAuthOrIsAdmin(userDetails, user.getUserCpf());
        return pets;
    }

    /**
     * Deleta um pet com base no ID.
     * Recupera o usuário autenticado.
     * Busca o Pet com base no ID.
     * Verifica se o usuário autenticado é ADMIN ou se o usuário autenticado é o mesmo usuário recuperado pelo pet.
     * (Verificação feita para apenas admins conseguirem visualizar ou usuários client visualizarem apenas o registro de seus pets).
     */
    @Override
    public void delete(Long id) {
        UserDetails userAuth = getUserAuth();
        Pets pets = petsRepository.findById(id).orElseThrow(() -> new PetsNotFound("Pet not found"));
        User user = userRepository.findById(pets.getClient().getId()).orElseThrow(() -> new PetsNotFound("User pets not found"));
        userAuthOrIsAdmin(userAuth, user.getUserCpf());
        petsRepository.deleteById(pets.getId());
    }


    @Override
    public Pets update(PetsDto petsDto) {
        UserDetails userAuth = getUserAuth();
        return saveUpdate(userAuth, petsDto);
    }


    private Pets saveUpdate(UserDetails userAuth, PetsDto petsDto) {
        User user = userRepository.findById(petsDto.getIdClient()).orElseThrow(() -> new PetsNotFound("No user record was found."));
        userAuthOrIsAdmin(userAuth, user.getUserCpf());
        Pets pets = petsMapper.toEntity(petsDto);
        pets.setBirthDate(petsDto.getBirthDate());
        pets.setDogName(petsDto.getDogName());
        return petsRepository.save(pets);
    }


    /**
     *********************************************************************************************************************************
     */

    // Métodos reutilizáveis
    
    private void userPetIsPresent(Pets pets) {
        Optional<User> userPet = userRepository.findById(pets.getClient().getId());
        if (userPet.isPresent()) {
            Optional<Pets> petExist = petsRepository.findByClientNameAndDogName(userPet.get().getFullName(), pets.getDogName());
            if (petExist.isPresent()) {
                throw new PetsAlreadyExist("Pet already exist. Please verify pet name and try again");
            }
        }
    }

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

    private void petsListIsEmpty(List<Pets> pets) {
        if (pets == null) {
            throw new PetsNotFound("No pets found.");
        }
    }
}
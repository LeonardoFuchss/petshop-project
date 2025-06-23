package com.project.petshop.petshop.service.impl.pets;

import com.project.petshop.petshop.domain.entities.Breed;
import com.project.petshop.petshop.domain.entities.Pets;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.domain.enums.Profile;
import com.project.petshop.petshop.dto.PetsDto;
import com.project.petshop.petshop.exceptions.pets.PetsAlreadyExist;
import com.project.petshop.petshop.mapper.PetsMapper;
import com.project.petshop.petshop.repository.PetsRepository;
import com.project.petshop.petshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PetsServiceImplTest {

    @Mock
    private PetsRepository petsRepository;
    @Mock
    private PetsMapper petsMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private PetsServiceImpl petsService;

    @Nested
    class savePet {

        @Test
        @DisplayName("Pet created successfully")
        void shouldSavePetWithSuccess() {
            User user = new User(1L, "05416199008", Profile.ADMIN, "Leonardo Dos Santos Fuchs", "testeSenha10", LocalDateTime.now());
            Pets pets = new Pets(1L, user, new Breed(1L, "yorkshire"), LocalDate.now(), "Leonardo Dos Santos Fuchs", "Beni", "yorkshire");
            PetsDto petsDto = new PetsDto(1L, 1L, LocalDate.now(), "Beni");

            when(petsMapper.toEntity(any(PetsDto.class))).thenReturn(pets);
            when(petsRepository.save(any(Pets.class))).thenReturn(pets);


            Pets petsSaved = petsService.save(petsDto);

            assertNotNull(petsSaved);
            assertEquals("Beni", petsSaved.getDogName());
        }
        @Test
        @DisplayName("Pets conflict")
        void shouldShowConflictException() {
            User user = new User(1L, "05416199008", Profile.ADMIN, "Leonardo Dos Santos Fuchs", "testeSenha10", LocalDateTime.now());
            Pets pets = new Pets(1L, user, new Breed(1L, "yorkshire"), LocalDate.now(), "Leonardo Dos Santos Fuchs", "Beni", "yorkshire");
            PetsDto petsDto = new PetsDto(1L, 1L, LocalDate.now(), "Beni");

            when(petsMapper.toEntity(any(PetsDto.class))).thenReturn(pets);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(petsRepository.findByClientNameAndDogName(user.getFullName(), pets.getDogName())).thenReturn(Optional.of(pets));

            assertThrows(PetsAlreadyExist.class, () -> petsService.save(petsDto));

        }
    }
}
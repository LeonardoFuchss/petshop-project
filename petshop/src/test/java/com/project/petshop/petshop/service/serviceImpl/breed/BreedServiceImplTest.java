package com.project.petshop.petshop.service.serviceImpl.breed;

import com.project.petshop.petshop.domain.entities.Breed;
import com.project.petshop.petshop.dto.BreedDto;
import com.project.petshop.petshop.exceptions.breed.BreedAlreadyExist;
import com.project.petshop.petshop.mapper.BreedMapper;
import com.project.petshop.petshop.repository.BreedRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BreedServiceImplTest {

    @Mock
    private BreedRepository breedRepository;
    @Mock
    private BreedMapper breedMapper;
    @InjectMocks
    private BreedServiceImpl breedService;

    @Nested
    class saveBreed {

        @Test
        @DisplayName("Saved Breed with successfully")
        void shouldSaveBreedWithSuccessfully() {
            Breed breed = new Breed(1L, "YorkShire");
            BreedDto breedDto = new BreedDto("YorkShire");

            doReturn(breed).when(breedRepository).save(breed);
            when(breedMapper.toEntity(breedDto)).thenReturn(breed);

            Breed savedBreed = breedService.save(breedDto);
            assertNotNull(savedBreed);
            assertEquals("YorkShire", savedBreed.getDescription());
        }
        @Test
        @DisplayName("Breed conflict")
        void shouldShowConflictException() {
            Breed breed = new Breed(1L, "YorkShire");
            BreedDto breedDto = new BreedDto("YorkShire");

            when(breedMapper.toEntity(breedDto)).thenReturn(breed);
            when(breedRepository.findByDescription(breed.getDescription())).thenReturn(breed);

            assertThrows(BreedAlreadyExist.class, () -> breedService.save(breedDto));
        }
    }
}
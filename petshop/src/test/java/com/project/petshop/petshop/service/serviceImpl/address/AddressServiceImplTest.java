package com.project.petshop.petshop.service.serviceImpl.address;

import com.project.petshop.petshop.domain.entities.Address;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.domain.enums.Profile;
import com.project.petshop.petshop.dto.AddressDto;
import com.project.petshop.petshop.mapper.AddressMapper;
import com.project.petshop.petshop.repository.AddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressMapper addressMapper;
    @InjectMocks
    private AddressServiceImpl addressService;

    @Nested
    class SaveAddress {

        @Test
        @DisplayName("Address created successfully")
        void shouldSaveAddressWithSuccess() {
            User user = new User(1L, "05416199008", Profile.ADMIN, "Leonardo Dos Santos Fuchs", "testeSenha10", LocalDateTime.now());
            Address address = new Address(1L, user, user.getFullName(), "Rua barão do rio branco", "Bento Gonçalves", "Apto 702");
            AddressDto addressDto = new AddressDto(1L, "Rua barão do rio branco", "Bento Gonçalves", "Apto 702");

            doReturn(address).when(addressRepository).save(address);
            when(addressMapper.toEntity(addressDto)).thenReturn(address);
            when(addressRepository.findByUser(user)).thenReturn(Optional.empty());
            when(addressRepository.save(address)).thenReturn(address);

            Address savedAddress = addressService.save(addressDto);

            assertNotNull(savedAddress);
            assertEquals("Rua barão do rio branco", address.getStreet());

        }
    }
}
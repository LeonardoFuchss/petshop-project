package com.project.petshop.petshop.mapper;

import com.project.petshop.petshop.dto.ServiceProvidedDto;
import com.project.petshop.petshop.model.entities.ServiceProvided;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ServiceProvidedMapper {

    public ServiceProvided toEntity(ServiceProvidedDto serviceProvidedDto) {

        return ServiceProvided.builder()
                .price(serviceProvidedDto.getPrice())
                .serviceName(serviceProvidedDto.getServiceName())
                .description(serviceProvidedDto.getDescription())
                .build();
    }
}

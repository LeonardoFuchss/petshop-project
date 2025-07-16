package com.project.petshop.petshop.service.interfaces.serviceprovided;

import com.project.petshop.petshop.dto.ServiceProvidedDto;
import com.project.petshop.petshop.model.entities.ServiceProvided;

import java.util.List;

public interface ServiceProvicedService {

    ServiceProvided createService(ServiceProvidedDto serviceProvidedDto);
    ServiceProvided findServiceById(Long id);
    List<ServiceProvided> findAllServices();
    void deleteServiceById(Long id);

}

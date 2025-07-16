package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.model.entities.ServiceProvided;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceProvidedRepository extends JpaRepository<ServiceProvided, Long> {
    Optional<ServiceProvided> findByServiceName(String serviceName);
    Optional<ServiceProvided> findByDescription (String description);
}

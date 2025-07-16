package com.project.petshop.petshop.service.impl.serviceprovided;

import com.project.petshop.petshop.dto.ServiceProvidedDto;
import com.project.petshop.petshop.exceptions.serviceprovided.ServiceAlreadyExist;
import com.project.petshop.petshop.exceptions.serviceprovided.ServiceNotFound;
import com.project.petshop.petshop.exceptions.user.UnauthorizedException;
import com.project.petshop.petshop.mapper.ServiceProvidedMapper;
import com.project.petshop.petshop.model.entities.ServiceProvided;
import com.project.petshop.petshop.repository.ServiceProvidedRepository;
import com.project.petshop.petshop.service.interfaces.serviceprovided.ServiceProvicedService;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServiceProvidedImpl implements ServiceProvicedService {
    private final ServiceProvidedRepository serviceProvidedRepository;
    private final ServiceProvidedMapper serviceProvidedMapper;

    @Override
    public ServiceProvided createService(ServiceProvidedDto serviceProvidedDto) {
        serviceAlreadyExist(serviceProvidedDto.getServiceName());
        UserDetails auth = getUserAuth();
        isAdmin(auth);
        ServiceProvided serviceProvided = serviceProvidedMapper.toEntity(serviceProvidedDto);
        return serviceProvidedRepository.save(serviceProvided);
    }

    @Override
    public ServiceProvided findServiceById(Long id) {
        UserDetails auth = getUserAuth();
        isAdmin(auth);
        Optional<ServiceProvided> serviceFind = Optional.ofNullable(serviceProvidedRepository.findById(id).orElseThrow(() -> new ServiceNotFound("Service not found.")));
        return serviceFind.get();
    }

    @Override
    public List<ServiceProvided> findAllServices() {
        UserDetails auth = getUserAuth();
        isAdmin(auth);
        return serviceProvidedRepository.findAll();
    }

    @Override
    public void deleteServiceById(Long id) {
        UserDetails auth = getUserAuth();
        isAdmin(auth);
        Optional<ServiceProvided> serviceProvided = serviceProvidedRepository.findById(id);
        serviceProvided.ifPresent(serviceProvidedRepository::delete);
    }
















    public void serviceAlreadyExist(String serviceName) {
        Optional<ServiceProvided> serviceFind = serviceProvidedRepository.findByServiceName(serviceName);
        if (serviceFind.isPresent()) {
            throw new ServiceAlreadyExist("This service already exists!");
        }
    }
    private void isAdmin(UserDetails userAuth) {
        boolean isAdmin = userAuth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new UnauthorizedException("You do not have permission to access this resource");
        }
    }
    private UserDetails getUserAuth() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

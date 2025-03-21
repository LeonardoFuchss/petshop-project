package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(User idUser);
    Optional<User> findByUserCpf(String cpf);
}

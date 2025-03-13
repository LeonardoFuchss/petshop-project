package com.project.petshop.petshop.repository;

import com.project.petshop.petshop.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}

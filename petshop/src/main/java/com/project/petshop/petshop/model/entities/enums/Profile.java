package com.project.petshop.petshop.model.entities.enums;

import lombok.Getter;

@Getter
public enum Profile {
    CLIENT("ROLE_CLIENT"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    Profile(String role) {
        this.role = role;
    }
}

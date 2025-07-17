package com.project.petshop.petshop.infra.auth;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthService {
    public boolean isAdmin(){
        return hasRole("ROLE_ADMIN");
    }
    public boolean isSelf(String cpf){
        UserDetails userDetails = getUserDetails();
        return userDetails.getUsername().equals(cpf);
    }
    private boolean hasRole(String role){
        return getUserDetails().getAuthorities().contains(new SimpleGrantedAuthority(role));
    }
    private UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

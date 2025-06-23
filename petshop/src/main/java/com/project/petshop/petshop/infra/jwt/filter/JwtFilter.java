package com.project.petshop.petshop.infra.jwt.filter;

import com.project.petshop.petshop.exceptions.user.InvalidTokenException;
import com.project.petshop.petshop.infra.jwt.service.JwtService;
import com.project.petshop.petshop.domain.entities.User;
import com.project.petshop.petshop.service.interfaces.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
/*
* Filtro responsável por interceptar todas as requisições e validar o token.
* Ele extrai o token do cabeçalho da requisição, verifica sua validade e autentica o usuário.
* Caso o token seja inválido ou ocorra algum erro, a requisição segue sem autenticação
* */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request); /* Extrai o token da requisição */
        if (token != null) {
            try {
                String cpf = jwtService.getCpfFromToken(token); /* Obtém o CPF do usuário a partir do token */
                Optional<User> user = userService.findUserByCpf(cpf); /* Busca o usuário do token no banco de dados */
                /* Se o usuário existir, define o usuário como autenticado */
                user.ifPresent(this::setUserAsAuthenticated);
            } catch (InvalidTokenException e) {
                log.error("Invalid JWT token: {}", token);
            } catch (Exception e) {
                log.error("Token validation error: {}", token);
            }
        }
        filterChain.doFilter(request, response); /* Continua o fluxo da requisição. */
    }

    private void setUserAsAuthenticated(User user) { /* Define o usuário como autenticado no contexto do Spring */
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUserCpf()) /* Define o CPF como nome de usuário */
                .password(user.getPassword()) /* Define a senha (Não é usada aqui, pois já foi autenticado) */
                .roles(String.valueOf(user.getProfile())) /* Define a role (permissão) do usuário */
                .build();

        /* Cria um token de autenticação do Spring Security */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        /* Define o usuário autenticado no contexto do Spring */
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


    private String getToken(HttpServletRequest request) { /* Extrai o token do cabeçalho Authorization da requisição */
        String authHeader = request.getHeader("Authorization");

        /* Verifica se o cabeçalho existe e está no formato correto (ex: "Bearer TOKEN") */
        if (authHeader != null) {
            String[] authHeaderParts = authHeader.split(" ", 2);
            if (authHeaderParts.length == 2) {
                return authHeaderParts[1]; /* Retorna apenas o token (sem o BEARER) */
            }
        }
        return null; /* Retorna null se não tiver um token válido. */
    }
}

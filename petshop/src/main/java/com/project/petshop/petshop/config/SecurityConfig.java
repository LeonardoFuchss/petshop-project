package com.project.petshop.petshop.config;

import com.project.petshop.petshop.config.filter.JwtFilter;
import com.project.petshop.petshop.jwt.JwtService;
import com.project.petshop.petshop.service.interfaces.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity /* Classe de controle de segurança da aplicação */
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() { /* Define o algoritmo de criptografia para senhas */
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtFilter jwtFilter(JwtService jwtService, UserService userService) {
        return new JwtFilter(jwtService, userService);  /* Cria um filtro JWT para autenticação. */
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

            return http
                    .csrf(AbstractHttpConfigurer::disable) /* Desabilita a proteção contra CSRF */
                    .cors(cors -> cors.configure(http)) /* Habilita CORS */
                    .authorizeHttpRequests(auth -> {
                        auth.requestMatchers( "/v3/api-docs/**",  // Documentação OpenAPI
                                "/swagger-ui/**",    // Interface Swagger UI
                                "/swagger-ui.html",  // Página do Swagger
                                "/swagger-resources/**", // Recursos internos do Swagger
                                "/webjars/**" // Dependências do Swagger
                        ).permitAll();
                        auth.requestMatchers("/swagger-resources/**").permitAll();
                        auth.requestMatchers("/users/delete/**").permitAll();
                        auth.requestMatchers("/users/**").permitAll();
                        auth.requestMatchers("/address/**").hasRole("ADMIN"); /* Limita acesso a admin */
                        auth.requestMatchers("/contact/**").hasRole("ADMIN"); /* Limita acesso a admin */
                        auth.requestMatchers("/breed/**").hasRole("ADMIN"); /* Limita acesso a admin */
                        auth.requestMatchers("/pets/**").hasAnyRole("ADMIN", "CLIENT");
                        auth.requestMatchers("/appointment/**").permitAll(); /* Limita acesso a admin */; /* Libera acesso ao público */
                        auth.anyRequest().authenticated();
                    })
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) /* Adiciona o filtro JWT antes do filtro padrão de autenticação */
                    .build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues(); /* Cria uma configuração padrão de cors, permitindo os valores padrão */

        UrlBasedCorsConfigurationSource cors = new UrlBasedCorsConfigurationSource(); /* Cria um objeto para gerenciar configurações de CORS com base nas URLs */
        cors.registerCorsConfiguration("/**", configuration); /* Aplica a configuração para todas as rotas do sistema. */

        return cors; /* Retorna a configuração de CORS para ser usada pelo Spring Security */
    }
}

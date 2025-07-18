package com.project.petshop.petshop.infra.config;

import com.project.petshop.petshop.infra.jwt.filter.JwtFilter;
import com.project.petshop.petshop.infra.jwt.service.JwtService;
import com.project.petshop.petshop.service.interfaces.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableWebSecurity /* Habilitando classe de controle de segurança da aplicação */
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
                        auth.requestMatchers("/users/delete/**").permitAll();
                        auth.requestMatchers("/users/**").permitAll();
                        auth.requestMatchers("/address/**").hasRole("ADMIN"); /* Limita acesso a admin */
                        auth.requestMatchers("/contact/**").hasRole("ADMIN"); /* Limita acesso a admin */
                        auth.requestMatchers("/breed/**").hasRole("ADMIN"); /* Limita acesso a admin */
                        auth.requestMatchers("/pets/**").hasAnyRole("ADMIN", "CLIENT");
                        auth.requestMatchers("/appointment/**").permitAll(); /* Libera acesso ao público */
                        auth.anyRequest().authenticated();
                    })
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) /* Adiciona o filtro JWT antes do filtro padrão de autenticação */
                    .build();
    }

    /**
     * Define regras que permitem que o backend aceite requisições de outras origens.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues(); /* Cria uma configuração padrão de cors, permitindo os valores padrões */
        UrlBasedCorsConfigurationSource cors = new UrlBasedCorsConfigurationSource(); /* Cria um objeto para gerenciar configurações de CORS com base nas URLs */
        cors.registerCorsConfiguration("/**", configuration); /* Aplica a configuração para todas as rotas do sistema. */
        return cors; /* Retorna a configuração de CORS para ser usada pelo Spring Security */
    }
}

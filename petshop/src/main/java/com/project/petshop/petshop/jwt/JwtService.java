package com.project.petshop.petshop.jwt;

import com.project.petshop.petshop.exceptions.user.InvalidTokenException;
import com.project.petshop.petshop.model.AccessToken;
import com.project.petshop.petshop.model.entities.User;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class JwtService { /* Classe de serviço do JWT para geração de tokens. */

    @Autowired
    private SecretKeyGenerator secretKey;

    public AccessToken generateToken(User user) { /* Função para gerar token de acesso. */

        SecretKey key = secretKey.getKey(); /* Obtém a chave para assinar o token */

        Date expirationDate = generationExpirationDate(); /* Busca a data de expiração da função */
        Map<String, Object> claims = generateTokenClaims(user); /* Informações (claims) adicionais ao token */

        String token = Jwts.builder() /* Gera token */
                .signWith(key) /* Assina o token com a chave adquirida na classe 'SecretKeyGenerator' */
                .subject(user.getUserCpf()) /* Identificador do usuário. */
                .expiration(expirationDate) /* Define data de expiração do token. */
                .claims(claims) /* Coloca as informações adicionais no token. */
                .compact();

        return new AccessToken(token);
    }

    private Date generationExpirationDate() { /* Define expiração do token */
        var expirationMinutes = 60;
        LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutes); /* Data atual + 60 minutos */
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant()); /* Define onde estamos no momento da requisição */
    }
    private Map<String, Object> generateTokenClaims(User user) { /* Gera informações adicionais do usuário para adicionar ao token */
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getUserCpf());
        return claims;
    }

    public String getCpfFromToken(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey.getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}

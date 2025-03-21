package com.project.petshop.petshop.jwt;

import com.project.petshop.petshop.exceptions.user.InvalidTokenException;
import com.project.petshop.petshop.domain.AccessToken;
import com.project.petshop.petshop.domain.entities.User;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService { /* Classe de serviço do JWT para geração de tokens. */

    @Autowired
    private SecretKeyGenerator secretKey;

    public AccessToken generateToken(User user) { /* Função para gerar token de acesso, recebendo o usuário chamado na rota de autenticação */

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
        var expirationMinutes = 60; /* minutos de expiração do token. */
        LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutes); /* Data atual + 60 minutos */
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant()); /* Define onde estamos no momento da requisição */
    }
    private Map<String, Object> generateTokenClaims(User user) { /* Gera informações adicionais do usuário para adicionar ao token (Posteriormente usado para decodificar o token e validar o usuário para autenticação (incluindo roles, permissões.)) */
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getUserCpf());
        return claims;
    }

    public String getCpfFromToken(String token) { /* Recebe o token da requisição e obtém o usuário a partir dele */
        try {
            /* Analisa e verifica o token JWT usando a chave secreta */
            return Jwts.parser()
                    .verifyWith(secretKey.getKey()) /* Usa a chave secreta para validar a assinatura */
                    .build()
                    .parseSignedClaims(token) /* Decodifica e valida o token */
                    .getPayload()
                    .getSubject(); /* Retorna o "subject" (neste caso, o CPF do usuário) */
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException("Invalid token"); /* Se o token for inválido, lança uma exceção */
        }
    }
}

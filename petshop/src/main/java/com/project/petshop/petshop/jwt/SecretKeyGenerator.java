package com.project.petshop.petshop.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class SecretKeyGenerator { /* Classe que gera uma chave para assinatura do token. */

    private SecretKey key; /* Armazena a chave. */

    public SecretKey getKey() { /* Adquirir a chave */
        if (key == null) {
            key = Jwts.SIG.HS256.key().build(); /* Gera a chave */
        }
        return key;
    }
}

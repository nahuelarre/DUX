package com.challenge.dux.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtGenerator {
    public static void main(String[] args) {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretKeyBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Generated Secret Key (Base64): " + secretKeyBase64);

        String jwtToken = Jwts.builder()
                .setSubject("1234567890")
                .setIssuer("Dux API")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Generated JWT Token: " + jwtToken);
    }
}


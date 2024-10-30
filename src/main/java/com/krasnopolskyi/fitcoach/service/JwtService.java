package com.krasnopolskyi.fitcoach.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Class provides functionality for generate and validate JWT token
 *
 */
@Service
public class JwtService {

    //unique key for generating token
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    //extract Username from header
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }


    public boolean isTokenValid(String token, String username) {
        final String userName = extractUserName(token);
        return (userName.equals(username)) && !isTokenExpired(token);
    }

    //return different claims from token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder().setClaims(extraClaims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis())) //setting date of granting token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // the token is valid for 10 minutes
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    //check token on Date
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Generate key from jwtSigningKey in application.jaml
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

package com.mk.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Tajny klucz do podpisywania tokenów, wczytywany z pliku application.properties
    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    // Czas życia tokenu, również wczytywany z application.properties
    @Value("${spring.jwt.expiration}")
    private long JWT_EXPIRATION;

    // generowanie nowego tokena na podstawie autoryzowanego użytkownika
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(claims) // dodawanie danych - ról
                .setSubject(userDetails.getUsername()) // nazwa użytkownika (username) jako subject
                .setIssuedAt(new Date(System.currentTimeMillis())) // kiedy token został wystawiony
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // kiedy token wygaśnie
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // podpisanie tokenu tajnym kluczem
                .compact(); // budowanie gotowego JWT
    }

    // wyciągnięcie username z tokena JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ogólna metoda pomocnicza do wyciągnięcia dowolnego "claim" z tokena (np. subject, expiration, itp.)
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // wyciąga wszystkie claims z tokena - payload z JWT
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // sprawdzenie, czy token jest poprawny (czy pasuje do użytkownika i nie wygasł)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // wyciągnięcie username z tokena
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // sprawdzenie, czy token już wygasł
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // wyciągnięcie daty wygaśnięcia tokena
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // tworzenie klucza szyfrującego na podsatwie tajnego klucza z .properties
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

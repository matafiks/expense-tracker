package com.mk.demo.config;

import com.mk.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    /**
     * Główna logika filtrowania — sprawdza nagłówek Authorization i ustawia użytkownika w kontekście
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // jeśli nie ma nagłówka lub nie zaczyna się od "Bearer ", pomiń filtr
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // wytnij token bez "Bearer "
        username = jwtService.extractUsername(jwt); // wyciągnij username z tokenu

        // jeśli użytkownik nie jest już zalogowany w kontekście i token zawiera nazwę użytkownika
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // jeśli token jest poprawny (czyli nie wygasł i pasuje do użytkownika)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // stwórz obiekt autoryzacji z użytkownikiem i jego rolami
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // dodatkowe szczegóły związane z żądaniem (np. IP, sesja)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // zapisanie użytkownika do kontekstu Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        filterChain.doFilter(request, response);
    }
}

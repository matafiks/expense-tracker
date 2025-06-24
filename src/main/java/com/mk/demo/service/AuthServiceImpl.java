package com.mk.demo.service;

import com.mk.demo.entity.Role;
import com.mk.demo.entity.User;
import com.mk.demo.repository.RoleRepository;
import com.mk.demo.repository.UserRepository;
import com.mk.demo.request.AuthRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Funkcja rejestrująca nowego użytkownika w bazie danych na podstawie informacji przekazanych w requście
     * @param authRequest dane przekazane do rejestracji nowego użytkownika
     */
    @Transactional
    @Override
    public void register(@Valid @RequestBody AuthRequest authRequest) {

        // sprawdź czy username z requestu już istnieje w bazie
        boolean dbUser = userRepository.existsByUsername(authRequest.getUsername());

        // jeżeli istnieje, wyrzuć wyjątek
        if (dbUser) {
            throw new RuntimeException("User with username " + authRequest.getUsername() + " already exists");
        }

        // pobierz domyślną rolę użytkownika ROLE_USER
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role does not exist"));

        // tworzenie nowego użytkownika na podstawie danych przekazanych w requeście (oraz hashowanie hasła)
        User user = User.builder()
                .username(authRequest.getUsername())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .roles(Set.of(defaultRole))
                .build();

        // zapis nowego usera do bazy danych
        userRepository.save(user);
    }

    /**
     * Funkcja służąca do autentykacji użytkownika z requestu. Jeżeli przejdzie pomyślnie - wygeneruj nowy token JWT
     * @param authRequest dane przekazane do autentykacji użytkownika
     * @return zwróć nowy token JWT
     */
    @Override
    public String login(@Valid @RequestBody AuthRequest authRequest) {

        // autentykacja usera
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            // jeśli username lub hasło są niepoprawne, zwróć wyjątek
            throw new RuntimeException("Incorrect username or password");
        }

        // pobierz dane użytkownika na potrzeby tokena
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // wygeneruj nowy token JWT dla zalogowanego użytkownika
        String jwtToken = jwtService.generateToken(new HashMap<>(), userDetails);

        // zwróć gotowy token JWT
        return jwtToken;
    }
}

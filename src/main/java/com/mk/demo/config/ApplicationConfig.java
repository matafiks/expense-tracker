package com.mk.demo.config;

import com.mk.demo.entity.User;
import com.mk.demo.repository.UserRepository;
import com.mk.demo.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Definicja jak ładować użytkownika na podstawie username
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new CustomUserDetails(user); // konwersja encji User na CustomUserDetails
        };
    }

    /**
     * Bean odpowiedzialny za sprawdzenie poprawności danych logowania
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // źródło danych użytkownika
        authProvider.setPasswordEncoder(passwordEncoder()); // sposób hashowania haseł (BCrypt)
        return authProvider;
    }

    /**
     * Hashowanie i weryfikowanie haseł (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean do uwierzytelnienia użytkownika
     * @return przekazanie konfiguracji do Spring Security
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // przekazanie konfiguracji do Spring Security
    }
}

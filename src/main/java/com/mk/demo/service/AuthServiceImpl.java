package com.mk.demo.service;

import com.mk.demo.entity.Role;
import com.mk.demo.entity.User;
import com.mk.demo.repository.RoleRepository;
import com.mk.demo.repository.UserRepository;
import com.mk.demo.request.AuthRequest;
import com.mk.demo.response.AuthResponse;
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
import java.util.HashSet;
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

    @Transactional
    @Override
    public void register(@Valid @RequestBody AuthRequest authRequest) {

        boolean dbUser = userRepository.existsByUsername(authRequest.getUsername());

        if (dbUser) {
            throw new RuntimeException("User with username " + authRequest.getUsername() + " already exists");
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role does not exist"));

        User user = User.builder()
                .username(authRequest.getUsername())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .roles(Set.of(defaultRole))
                .build();

        userRepository.save(user);
    }

    @Override
    public String login(@Valid @RequestBody AuthRequest authRequest) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new RuntimeException("Incorrect username or password");
        }

//        User user = userRepository.findByUsername(authRequest.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
//
//        UserDetails userDetails = new CustomUserDetails(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        String jwtToken = jwtService.generateToken(new HashMap<>(), userDetails);

        return jwtToken;
    }
}

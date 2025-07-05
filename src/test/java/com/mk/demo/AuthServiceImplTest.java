package com.mk.demo;

import com.mk.demo.entity.Role;
import com.mk.demo.entity.User;
import com.mk.demo.repository.RoleRepository;
import com.mk.demo.repository.UserRepository;
import com.mk.demo.request.AuthRequest;
import com.mk.demo.service.AuthServiceImpl;
import com.mk.demo.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authRequest = AuthRequest.builder()
                .username("john")
                .password("Secret123!")
                .build();
    }

    @Test
    void shouldRegisterNewUser() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("Secret123!")).thenReturn("hashedSecret");

        authService.register(authRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("john");
        assertThat(savedUser.getPassword()).isEqualTo("hashedSecret");
        assertThat(savedUser.getRoles()).contains(role);
    }

    @Test
    void shouldThrowIfUserAlreadyExists() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(authRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldThrowIfRoleDoesNotExist() {
        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.register(authRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Role does not exist");
    }

    @Test
    void shouldAuthenticateAndReturnToken() {
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);
        when(jwtService.generateToken(anyMap(), eq(userDetails))).thenReturn("mocked.jwt.token");

        String token = authService.login(authRequest);

        assertThat(token).isEqualTo("mocked.jwt.token");
    }


    @Test
    void shouldThrowWhenAuthenticationFails() {
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThatThrownBy(() -> authService.login(authRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Incorrect username or password");

        verify(jwtService, never()).generateToken(anyMap(), any());
    }
}

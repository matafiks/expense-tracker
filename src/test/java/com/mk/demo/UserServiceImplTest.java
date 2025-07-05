package com.mk.demo;

import com.mk.demo.entity.Role;
import com.mk.demo.entity.User;
import com.mk.demo.repository.UserRepository;
import com.mk.demo.response.UserResponse;
import com.mk.demo.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userRole = new Role();
        userRole.setId(1L);
        userRole.setName("USER");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("john");
        mockUser.setRoles(Set.of(userRole));
    }

    @Test
    void shouldReturnUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        UserResponse result = userService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("john");
        assertThat(result.getRoles()).contains(userRole);

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Did not find user with id: 99");
    }

    @Test
    void shouldReturnAllUsers() {
        User anotherUser = new User();
        Role adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName("ADMIN");

        anotherUser.setId(2L);
        anotherUser.setUsername("alice");
        anotherUser.setRoles(Set.of(adminRole));

        when(userRepository.findAll()).thenReturn(List.of(mockUser, anotherUser));

        List<UserResponse> result = userService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("john");
        assertThat(result.get(1).getUsername()).isEqualTo("alice");
    }

    @Test
    void shouldDeleteUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        userService.deleteById(1L);

        verify(userRepository).delete(mockUser);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentUser() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteById(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Did not find user with id: 999");

        verify(userRepository, never()).delete(any());
    }
}
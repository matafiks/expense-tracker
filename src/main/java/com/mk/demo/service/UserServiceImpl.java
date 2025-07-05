package com.mk.demo.service;

import com.mk.demo.entity.User;
import com.mk.demo.repository.UserRepository;
import com.mk.demo.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Funkcja wyszukująca w bazie danych użytkownika o zadanym id
     * @param id
     * @return Zwraca tylko najważniejsze dane użytkownika w postaci UserResponse
     */
    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find user with id: " + id));
        return convertToUserResponse(user);
    }

    /**
     * Funkcja do wyszukiwania wszystkich użytkowników w bazie danych
     * @return Zwraca listę użytkowników w formie UserResponse
     */
    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .toList();
    }

    /**
     * Funkcja do usuwania użytkownika z bazy danych na podstawie id
     * @param id
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find user with id: " + id));
        userRepository.delete(user);
    }

    /**
     * Metoda pomocnicza do konwersji User na UserResponse, aby ukryć dane wrażliwe jak np. hasło
     * @param user
     * @return Zwraca przekonwertowanego UserResponse
     */
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }
}

package com.mk.demo.service;

import com.mk.demo.entity.User;
import com.mk.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {

        if (id == null || id < 1) {
            throw new IllegalArgumentException("User id must be greater than 1 and not equal to null");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Did not find user with id: " + id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("User id must be greater than 1 and not equal to null");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }
}

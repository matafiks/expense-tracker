package com.mk.demo.service;

import com.mk.demo.entity.User;
import com.mk.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User save(User user) {
        // use bcrypt to hash user password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // save user to database
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {

        // make sure id must be greater than 1 and not equal to null
        if (id == null || id < 1) {
            throw new IllegalArgumentException("User id must be greater than 1 and not equal to null");
        }

        // if id is valid, find user by id in database, else print message
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
        // make sure id must be greater than 1 and not equal to null

        if (id == null || id < 1) {
            throw new IllegalArgumentException("User id must be greater than 1 and not equal to null");
        }
        // if id is valid, delete user by id
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }
}

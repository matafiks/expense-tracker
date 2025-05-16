package com.mk.demo.controller;

import com.mk.demo.entity.User;
import com.mk.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Find user by id")
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @Operation(summary = "List all existing users")
    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @Operation(summary = "Delete user by id")
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}

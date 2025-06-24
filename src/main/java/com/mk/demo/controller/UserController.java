package com.mk.demo.controller;

import com.mk.demo.entity.User;
import com.mk.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User REST API endpoints", description = "Operations related to user accounts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // TODO: make sure only admin can do stuff here

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

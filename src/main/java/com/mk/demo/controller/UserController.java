package com.mk.demo.controller;

import com.mk.demo.response.UserResponse;
import com.mk.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User REST API endpoints", description = "Operations related to user accounts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Find user by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable @Min(1) Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @Operation(summary = "List all existing users")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Delete user by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable @Min(1) Long userId) {
        userService.deleteById(userId);
        return ResponseEntity.ok("User with id: " + userId + " has been deleted successfully");
    }
}

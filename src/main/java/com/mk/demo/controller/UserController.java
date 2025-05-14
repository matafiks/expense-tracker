package com.mk.demo.controller;

import com.mk.demo.entity.User;
import com.mk.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // dont need it as im using Lombok with @RequiredArgsConstructor but want to keep it to remember
    /*@Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }*/

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}

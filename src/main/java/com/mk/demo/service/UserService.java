package com.mk.demo.service;

import com.mk.demo.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse findById(Long id);

    List<UserResponse> findAll();

    void deleteById(Long id);
}

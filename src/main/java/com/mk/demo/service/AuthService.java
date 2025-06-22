package com.mk.demo.service;

import com.mk.demo.request.AuthRequest;
import com.mk.demo.response.AuthResponse;

public interface AuthService {

    void register(AuthRequest authRequest);
    String login(AuthRequest authRequest);

}

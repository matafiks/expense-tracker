package com.mk.demo.service;

import com.mk.demo.request.AuthRequest;

public interface AuthService {

    void register(AuthRequest authRequest);
    String login(AuthRequest authRequest);

}

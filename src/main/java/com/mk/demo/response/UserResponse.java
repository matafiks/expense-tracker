package com.mk.demo.response;

import com.mk.demo.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Builder
@Getter
@Setter
public class UserResponse {

    private Long id;
    private String username;
    private Set<Role> roles;
}

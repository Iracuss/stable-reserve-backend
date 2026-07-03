package com.starace.stable_manager.dto;

import com.starace.stable_manager.enums.Role;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
}

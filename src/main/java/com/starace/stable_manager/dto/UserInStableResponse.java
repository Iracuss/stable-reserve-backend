package com.starace.stable_manager.dto;

import com.starace.stable_manager.enums.MembershipRole;

import lombok.Data;

@Data
public class UserInStableResponse {
    private Long userId;
    private String username;
    private String email;
    private MembershipRole role;
}

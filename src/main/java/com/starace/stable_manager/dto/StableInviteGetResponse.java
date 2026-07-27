package com.starace.stable_manager.dto;

import com.starace.stable_manager.enums.MembershipRole;

import lombok.Data;

@Data
public class StableInviteGetResponse {
    private Long stableId;
    private String stableName;
    private String stableOwner;
    private MembershipRole stableRole;
}

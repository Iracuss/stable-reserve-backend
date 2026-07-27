package com.starace.stable_manager.dto;

import lombok.Data;

@Data
public class StableInviteGetResponse {
    private Long stableId;
    private String stableName;
    private String stableOwner;
}

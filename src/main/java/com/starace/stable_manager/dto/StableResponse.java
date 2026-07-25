package com.starace.stable_manager.dto;

import com.starace.stable_manager.embeddable.StablePreferences;

import lombok.Data;

@Data
public class StableResponse {
    private Long id;
    private String name;
    private StablePreferences preferences;
    private String ownerUsername;
    private int memberCount;
    private int horseCount;
    private String currentUserRole;
}

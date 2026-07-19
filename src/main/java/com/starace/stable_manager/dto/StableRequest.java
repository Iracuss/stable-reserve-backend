package com.starace.stable_manager.dto;

import com.starace.stable_manager.embeddable.StablePreferences;

import lombok.Data;

@Data
public class StableRequest {
    private String name;
    private StablePreferences preferences;
}

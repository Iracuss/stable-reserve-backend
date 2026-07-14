package com.starace.stable_manager.dto;

import java.util.List;

import com.starace.stable_manager.enums.AlertType;

import lombok.Data;

@Data
public class HorseAlert {
    private String horseName;
    private List<AlertType> alerts;
}

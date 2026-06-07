package com.starace.stable_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starace.stable_manager.model.HealthRecord;
import com.starace.stable_manager.service.HealthRecordService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/records")
public class HealthRecordController {

    @Autowired
    private HealthRecordService healthRecordService;

    @PostMapping("/horse/{horseId}")
    public HealthRecord addRecord(@PathVariable Long horseId, @RequestBody HealthRecord record, @RequestParam Long staffId) {
        return healthRecordService.addRecordToHorse(horseId, staffId, record);
    }
    
    @GetMapping("/horse/{horseId}")
    public List<HealthRecord> getHorseHistory(@PathVariable Long horseId) {
        return healthRecordService.getRecordsByHorse(horseId);
    }
    
    @PutMapping("/{recordId}")
    public HealthRecord updateHealthRecord(@PathVariable Long recordId, @RequestBody HealthRecord recordDetails) {
        return healthRecordService.updateHealthRecord(recordId, recordDetails);
    }

    @DeleteMapping("/{recordId}")
    public void deleteHealthRecord(@PathVariable Long recordId) {
        healthRecordService.deleteHealthRecord(recordId);
    }
}

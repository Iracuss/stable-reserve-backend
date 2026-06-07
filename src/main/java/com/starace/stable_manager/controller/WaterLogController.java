package com.starace.stable_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starace.stable_manager.model.WaterLog;
import com.starace.stable_manager.service.WaterLogService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/water-logs")
public class WaterLogController {
    @Autowired
    private WaterLogService waterLogService;

    @GetMapping("/horse/{horseId}")
    public List<WaterLog> getHorseHistory(@PathVariable Long horseId) {
        return waterLogService.getAllWaterLogs(horseId);
    }

    @PostMapping("/horse/{horseId}")
    public WaterLog addRecord(@PathVariable Long horseId, @RequestBody WaterLog log, @RequestParam Long staffId) {
        return waterLogService.addNewWaterLog(log, horseId, staffId);
    }
    
    @PutMapping("/{id}")
    public WaterLog updateWaterLog(@PathVariable Long id, @RequestBody WaterLog log) {
        return waterLogService.updateWaterLog(id, log);
    }

    @DeleteMapping("/{id}")
    public void deleteWaterLog(@PathVariable Long id) {
        waterLogService.deleteWaterLog(id);
    }
    
}

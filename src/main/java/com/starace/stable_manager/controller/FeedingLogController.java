package com.starace.stable_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starace.stable_manager.model.FeedingLog;
import com.starace.stable_manager.service.FeedingLogService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/feeding-logs")
public class FeedingLogController {

    @Autowired
    private FeedingLogService feedingLogService;

    @GetMapping("/horse/{horseId}")
    public List<FeedingLog> getAllFeedingLogs(@PathVariable Long horseId) {
        return feedingLogService.getAllFeedingLogs(horseId);
    }
    
    @PostMapping("/horse/{horseId}")
    public FeedingLog addFeedingLog(@PathVariable Long horseId, @RequestBody FeedingLog log, @RequestParam Long staffId) {
        
        return feedingLogService.addNewFeedingLog(horseId, log, staffId);
    }

    @PutMapping("/{id}")
    public FeedingLog updateFeedingLog(@PathVariable Long id, @RequestBody FeedingLog feeding) {
        return feedingLogService.updateFeedingLog(id, feeding);
    }

    @DeleteMapping("/{id}")
    public void deleteFeedingLog(@PathVariable Long id) {
        feedingLogService.deleteFeedingLog(id);
    }
}

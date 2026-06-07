package com.starace.stable_manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.model.FeedingLog;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.model.Staff;
import com.starace.stable_manager.repository.FeedingLogRepository;
import com.starace.stable_manager.repository.HorseRepository;
import com.starace.stable_manager.repository.StaffRepository;

@Service
public class FeedingLogService {
    
    @Autowired
    private HorseRepository horseRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private FeedingLogRepository feedingLogRepository;

    public List<FeedingLog> getAllFeedingLogs(Long horseId) {
        if(!horseRepository.existsById(horseId)) {
            throw new RuntimeException("Cannot fetch feeding logs. Horse not found: " + horseId);
        }
        return feedingLogRepository.findByHorseId(horseId);
    }

    public FeedingLog addNewFeedingLog(Long horseId, FeedingLog log, Long staffId) {
        Horse horse = horseRepository.findById(horseId)
            .orElseThrow(() -> new RuntimeException("Horse not found with id: " + horseId));
        
        Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        log.setHorse(horse);
        log.setRecordedBy(staff);
        log.setFedAt(LocalDateTime.now());

        return feedingLogRepository.save(log);
    }

    public FeedingLog saveFeedingLog(FeedingLog feeding) {
        return feedingLogRepository.save(feeding);
    }

    public FeedingLog updateFeedingLog(Long id, FeedingLog feeding) {
        return feedingLogRepository.findById(id).map(log -> {
            if(feeding.getAmountLbs() != null) log.setAmountLbs(feeding.getAmountLbs());
            if(feeding.getFedAt() != null) log.setFedAt(feeding.getFedAt());
            if(feeding.getFeedType() != null) log.setFeedType(feeding.getFeedType());
            if(feeding.getPurpose() != null) log.setPurpose(feeding.getPurpose());
            if(feeding.getHorse() != null) log.setHorse(feeding.getHorse());
            if(feeding.getRecordedBy() != null) log.setRecordedBy(feeding.getRecordedBy());

            return feedingLogRepository.save(log);
        }).orElseThrow(() -> new RuntimeException("Log not found: " + id));
    }

    public void deleteFeedingLog(Long id) {
        if(!feedingLogRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Feeding log not found with id: " + id);
        }
        feedingLogRepository.deleteById(id);
    }
}

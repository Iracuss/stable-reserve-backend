package com.starace.stable_manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.model.Staff;
import com.starace.stable_manager.model.WaterLog;
import com.starace.stable_manager.repository.HorseRepository;
import com.starace.stable_manager.repository.StaffRepository;
import com.starace.stable_manager.repository.WaterLogRepository;

@Service
public class WaterLogService {
    @Autowired
    private WaterLogRepository waterLogRepository;

    @Autowired
    private HorseRepository horseRepository;

    @Autowired
    private StaffRepository staffRepository;
    // get all
    // post log
    // update log
    // delete log

    // Specific to a horse
    public List<WaterLog> getAllWaterLogs(Long horseId) {
        if(!horseRepository.existsById(horseId)){
            throw new RuntimeException("Cannot fetch water logs. Horse not found: " + horseId);
        }
        return waterLogRepository.findByHorseId(horseId);
    }

    // Post a log to a horse
    public WaterLog addNewWaterLog(WaterLog log, Long horseId, Long staffId){
        Horse horse = horseRepository.findById(horseId)
            .orElseThrow(() -> new RuntimeException("Horse not found with id: " + horseId));
        
        Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        log.setHorse(horse);
        log.setRecordedBy(staff);
        log.setRecordedAt(LocalDateTime.now());

        return waterLogRepository.save(log);
    }

    // this should be for updating a log
    public WaterLog updateWaterLog(Long id, WaterLog log) {
        return waterLogRepository.findById(id).map(waterLog -> {
            if(log.getAmountLbs() != null) waterLog.setAmountLbs(log.getAmountLbs());
            if(log.getPurpose() != null) waterLog.setPurpose(log.getPurpose());
            if(log.getRecordedAt() != null) waterLog.setRecordedAt(log.getRecordedAt());
            if(log.getHorse() != null) waterLog.setHorse(log.getHorse());
            if(log.getRecordedBy() != null) waterLog.setRecordedBy(log.getRecordedBy());
            return waterLogRepository.save(waterLog);
        }).orElseThrow(() -> new RuntimeException("Record not found: " + id));
    }

    // delete a log
    public void deleteWaterLog(Long id){
        if(!waterLogRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Water log not found with id: " + id);
        }
    }
}

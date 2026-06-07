package com.starace.stable_manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.model.HealthRecord;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.model.Staff;
import com.starace.stable_manager.repository.HealthRecordRepository;
import com.starace.stable_manager.repository.HorseRepository;
import com.starace.stable_manager.repository.StaffRepository;

@Service
public class HealthRecordService {
    
    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private HorseRepository horseRepository;

    @Autowired
    private StaffRepository staffRepository;

    public HealthRecord addRecordToHorse(Long horseId, Long staffId, HealthRecord record) {
        Horse horse = horseRepository.findById(horseId)
            .orElseThrow(() -> new RuntimeException("Horse not found with id: " + horseId));

        Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        record.setHorse(horse);
        record.setRecordedBy(staff);
        record.setRecordedAt(LocalDateTime.now());

        if(record.getTemperature() > 102.0) {
            System.out.println("ALERT: Horse " + horse.getName() + " has a fever!");
        }

        return healthRecordRepository.save(record);
    }

    public List<HealthRecord> getRecordsByHorse(Long horseId) {
        if(!horseRepository.existsById(horseId)) {
            throw new RuntimeException("Cannot fetch records. Horse not found: " + horseId);
        }
        return healthRecordRepository.findByHorseId(horseId);
    }

    public HealthRecord saveRecord(HealthRecord record) {
        return healthRecordRepository.save(record);
    }

    public HealthRecord updateHealthRecord(Long recordId, HealthRecord recordDetails) {
        //update the whole thing
        return healthRecordRepository.findById(recordId).map(record -> {
            if(recordDetails.getHeartRate() != null) record.setHeartRate(recordDetails.getHeartRate());
            if(recordDetails.getRecordedAt() != null) record.setRecordedAt(recordDetails.getRecordedAt());
            if(recordDetails.getTemperature() != null) record.setTemperature(recordDetails.getTemperature());
            if(recordDetails.getWeight() != null) record.setWeight(recordDetails.getWeight());
            if(recordDetails.getRespirationRate() != null) record.setRespirationRate(recordDetails.getRespirationRate());
            if(recordDetails.getRecordedBy() != null) record.setRecordedBy(recordDetails.getRecordedBy());
            if(recordDetails.getHorse() != null) record.setHorse(recordDetails.getHorse());
            
            return healthRecordRepository.save(record);
        }).orElseThrow(() -> new RuntimeException("Record not found: " + recordId));
    }

    public void deleteHealthRecord(Long id) {
        if(!healthRecordRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Health record not found with id: " + id);
        }
        healthRecordRepository.deleteById(id);
    }
}

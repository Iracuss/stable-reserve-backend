package com.starace.stable_manager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.repository.HorseRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HorseService {
    @Autowired
    private HorseRepository horseRepository;

    public Horse saveHorse(Horse horse) {
        checkHealthAlerts(horse);
        return horseRepository.save(horse);
    }

    public List<Horse> getAllHorses() {
        List<Horse> horses = horseRepository.findAll();

        horses.forEach(this::checkHealthAlerts); // check each horses need for something
        return horses;
    }

    public Horse getHorseById(Long id) {
        return horseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Horse not found with id: " + id));
    }

    public void checkHealthAlerts(Horse horse) {
        if(horse.getLastCogginDate() != null) {
            if(horse.getLastCogginDate().isBefore(LocalDate.now().minusYears(1))) {
                System.out.println("LEGAL ALERT: " + horse.getName() + " has an EXPIRED Coggins test!");
            }
        }

        if(horse.getLastFarrierDate() != null) {
            if(horse.getLastFarrierDate().isBefore(LocalDate.now().minusWeeks(6))) {
                System.out.println("MAINTENANCE ALERT: " + horse.getName() + " is overdue for the Farrier!");
            }
        }
    }

    public int calculateRacingAge(Horse horse) {
        if (horse.getBirthYear() == null) return 0;
        
        int currentYear = LocalDate.now().getYear();
        // Racing age formula
        return currentYear - horse.getBirthYear();
    }

    public Horse updateHorse(Long id, Horse horseDetails) {
        return horseRepository.findById(id).map(horse -> {
            if(horseDetails.getName() != null) horse.setName(horseDetails.getName());
            if(horseDetails.getBreed() != null) horse.setBreed(horseDetails.getBreed());
            if(horseDetails.getBirthYear() != null) horse.setBirthYear(horseDetails.getBirthYear());
            if(horseDetails.getNickname() != null) horse.setNickname(horseDetails.getNickname());
            if(horseDetails.getMicrochipId() != null) horse.setMicrochipId(horseDetails.getMicrochipId());
            if(horseDetails.getIsMdBred() != null) horse.setIsMdBred(horseDetails.getIsMdBred());
            if(horseDetails.getFoalingState() != null) horse.setFoalingState(horseDetails.getFoalingState());
            if(horseDetails.getLastCogginDate() != null) horse.setLastCogginDate(horseDetails.getLastCogginDate());
            if(horseDetails.getLastFarrierDate() != null) horse.setLastFarrierDate(horseDetails.getLastFarrierDate());
            if(horseDetails.getMedicalNotes() != null) horse.setMedicalNotes(horseDetails.getMedicalNotes());
            
            checkHealthAlerts(horse);
            return horseRepository.save(horse);
        }).orElseThrow(() -> new RuntimeException("Horse not found with id " + id));
    }

    public void deleteHorse(Long id) {
        if(!horseRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Horse not found with id: " + id);
        }
        horseRepository.deleteById(id);
    }
}

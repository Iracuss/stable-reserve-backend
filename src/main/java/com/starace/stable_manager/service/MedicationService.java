package com.starace.stable_manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.model.Medication;

import com.starace.stable_manager.repository.HorseRepository;
import com.starace.stable_manager.repository.MedicationRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MedicationService {
    
    @Autowired
    private HorseRepository horseRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    public List<Medication> getAllMedications(Long horseId) {
        if(!horseRepository.existsById(horseId)) {
            throw new RuntimeException("Cannot fetch medication. Horse not found: " + horseId);
        }
        return medicationRepository.findByHorseId(horseId);
    }

    public Medication getMedicationById(Long id) {
        return medicationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Medication not found with id: " + id));
    }

    public Medication addNewMedication(Long horseId, Medication med) {
        Horse horse = horseRepository.findById(horseId)
            .orElseThrow(() -> new RuntimeException("Horse not found with id: " + horseId));
        
        med.setHorse(horse);
        return medicationRepository.save(med);
    }

    public Medication saveMedication(Medication med) {
        return medicationRepository.save(med);
    }

    public Medication updateMedication(Long id, Medication med) {
        return medicationRepository.findById(id).map(medication -> {
            if(med.getDosage() != null) medication.setDosage(med.getDosage());
            if(med.getFrequency() != null) medication.setFrequency(med.getFrequency());
            if(med.getName() != null) medication.setName(med.getName());
            if(med.getStartDate() != null) medication.setStartDate(med.getStartDate());
            if(med.getEndDate() != null) medication.setEndDate(med.getEndDate());
            if(med.getHorse() != null) medication.setHorse(med.getHorse());

            return medicationRepository.save(medication);
        }).orElseThrow(() -> new RuntimeException("Medication not found: " + id));
    }

    public void deleteMedication(Long id) {
        if(!medicationRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Medication not found with id: " + id);
        }
        medicationRepository.deleteById(id);
    }
}

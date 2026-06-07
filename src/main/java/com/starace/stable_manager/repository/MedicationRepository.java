package com.starace.stable_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starace.stable_manager.model.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long>{
    List<Medication> findByHorseId(Long horseId);
}

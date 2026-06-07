package com.starace.stable_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starace.stable_manager.model.WaterLog;

@Repository
public interface WaterLogRepository extends JpaRepository<WaterLog, Long>{
    List<WaterLog> findByHorseId(Long horseId);
}

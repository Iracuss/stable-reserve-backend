package com.starace.stable_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starace.stable_manager.model.FeedingLog;

@Repository
public interface FeedingLogRepository extends JpaRepository<FeedingLog, Long>{
    List<FeedingLog> findByHorseId(Long horseId);
}

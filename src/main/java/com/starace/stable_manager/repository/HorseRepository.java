package com.starace.stable_manager.repository;

import com.starace.stable_manager.model.Horse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long>{

}

package com.starace.stable_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starace.stable_manager.model.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>{
    
}

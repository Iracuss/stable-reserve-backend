package com.starace.stable_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starace.stable_manager.model.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    
}

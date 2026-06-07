package com.starace.stable_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starace.stable_manager.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>{
    
}

package com.starace.stable_manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.model.Owner;

import com.starace.stable_manager.repository.OwnerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;

    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    public Owner getOwnerById(Long id) {
        return ownerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + id));
    }

    public Owner saveOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

    public Owner updateOwner(Long id, Owner owner) {
        return ownerRepository.findById(id).map(own -> {
            if(owner.getName() != null) own.setName(owner.getName());
            if(owner.getEmergencyContact() != null) own.setEmergencyContact(owner.getEmergencyContact());
            if(owner.getPhoneNumber() != null) own.setPhoneNumber(owner.getPhoneNumber());
            return ownerRepository.save(own);
        }).orElseThrow(() -> new RuntimeException("Owner not found: " + id));
    }

    public void deleteOwner(Long id) {
        if(!ownerRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Owner not found with id: " + id);
        }
        ownerRepository.deleteById(id);
    }
}

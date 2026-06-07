package com.starace.stable_manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.model.Stable;
import com.starace.stable_manager.repository.StableRepository;

@Service
public class StableService {
    @Autowired
    private StableRepository stableRepository;
    // get all
    // get one with id (soon)
    // post log
    // update log
    // delete log

    // get all stables
    public List<Stable> getAllStables() {
        return stableRepository.findAll();
    }

    // make a stable
    public Stable addNewStable(Stable stable){
        return stableRepository.save(stable);
    }

    // update the stables
    public Stable updateStable(long id, Stable newStable) {
        return stableRepository.findById(id).map(stable -> {
            if(newStable.getAddress() != null) stable.setAddress(newStable.getAddress());
            if(newStable.getStaffMembers() != null) stable.setStaffMembers(newStable.getStaffMembers());
            if(newStable.getHorses() != null) stable.setHorses(newStable.getHorses());
            return stableRepository.save(stable);
        }).orElseThrow(() -> new RuntimeException("Stable not found: " + id));
    }

    // delete a stable
    public void deleteStable(Long id){
        if(!stableRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Stable not found with id: " + id);
        }
    }
}

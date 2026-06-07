package com.starace.stable_manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.model.Staff;
import com.starace.stable_manager.repository.StaffRepository;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;
    // get all
    // post log
    // update log
    // delete log

    // Get all
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    // make a new staff
    public Staff addNewStaff(Staff staff){
        // Check if staff's stable id is correct?
        return staffRepository.save(staff);
    }

    // update staff
    public Staff updateStaff(Long id, Staff newStaff) {
        return staffRepository.findById(id).map(staff -> {
            if(newStaff.getContactInfo() != null) staff.setContactInfo(newStaff.getContactInfo());
            if(newStaff.getName() != null) staff.setName(newStaff.getName());
            if(newStaff.getRole() != null) staff.setRole(newStaff.getRole());
            if(newStaff.getUserEmail() != null) staff.setUserEmail(newStaff.getUserEmail());
            if(newStaff.getStable() != null) staff.setStable(newStaff.getStable());
            
            return staffRepository.save(staff);
        }).orElseThrow(() -> new RuntimeException("Staff not found: " + id));
    }

    // delete a log
    public void deleteStaff(Long id){
        if(!staffRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Staff not found with id: " + id);
        }
    }
}

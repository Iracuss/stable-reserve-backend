package com.starace.stable_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starace.stable_manager.model.Stable;
import com.starace.stable_manager.service.StableService;

@RestController
@RequestMapping("/api/stables")
public class StableController {
    @Autowired
    private StableService stableService;

    @GetMapping
    public List<Stable> getStables() {
        return stableService.getAllStables();
    }

    @PostMapping
    public Stable addStable(@RequestBody Stable stable) {
        return stableService.addNewStable(stable);
    }
    
    @PutMapping("/{id}")
    public Stable updateStable(@PathVariable Long id, @RequestBody Stable stable) {
        return stableService.updateStable(id, stable);
    }

    @DeleteMapping("/{id}")
    public void deleteStable(@PathVariable Long id) {
        stableService.deleteStable(id);
    }
    
}

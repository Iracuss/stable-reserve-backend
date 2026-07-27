package com.starace.stable_manager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starace.stable_manager.dto.StableAcceptRequest;
import com.starace.stable_manager.dto.StableInviteRequest;
import com.starace.stable_manager.dto.StableRequest;
import com.starace.stable_manager.service.StableService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/stables")
@RequiredArgsConstructor
public class StableController {
    private final StableService stableService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getStableById(@PathVariable Long id) {
        return ResponseEntity.ok(stableService.getStableById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllUserStables() {
        return ResponseEntity.ok(stableService.getAllUserStables());
    }
    
    @PostMapping
    public ResponseEntity<?> createStable(@RequestBody StableRequest request) {
        return ResponseEntity.ok(stableService.createStable(request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStable(@PathVariable Long id, @RequestBody StableRequest request) {
        return ResponseEntity.ok(stableService.updateStable(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStable(@PathVariable Long id) {
        stableService.deleteStable(id);
        return ResponseEntity.ok("Stable deleted successfully");
    }
    
    @PostMapping("/invites/{stableId}")
    public ResponseEntity<?> sendStableInvite(@PathVariable Long stableId, @RequestBody StableInviteRequest request) {
        stableService.sendStableInvite(stableId, request);
        return ResponseEntity.ok("Invite Sent");
    }

    // Maybe in users or invite controller?
    @GetMapping("/invites/me")
    public ResponseEntity<?> getAllUserInvites() {
        return ResponseEntity.ok(stableService.getAllUserInvites());
    }

    @PutMapping("/invites/{stableId}/accept")
    public ResponseEntity<?> acceptStableInvite(@PathVariable Long stableId, @RequestBody StableAcceptRequest request) {
        stableService.acceptStableInvite(stableId, request);
        return ResponseEntity.ok("Invite processed");
    }
}

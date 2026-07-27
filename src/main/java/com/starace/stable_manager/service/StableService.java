package com.starace.stable_manager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.StableAcceptRequest;
import com.starace.stable_manager.dto.StableInviteGetResponse;
import com.starace.stable_manager.dto.StableInviteRequest;
import com.starace.stable_manager.dto.StableRequest;
import com.starace.stable_manager.dto.StableResponse;
import com.starace.stable_manager.enums.MembershipRole;
import com.starace.stable_manager.model.Membership;
import com.starace.stable_manager.model.Stable;
import com.starace.stable_manager.model.User;
import com.starace.stable_manager.repository.StableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StableService {
    private final StableRepository stableRepository;
    private final CurrentUserService currentUserService;
    private final MembershipService membershipService;
    private final UserService userService;
    // private final EmailService emailService;

    @Value("${frontend.url}")
    private String frontendUrl;

    private StableResponse mapToResponse(Stable stable) {
        StableResponse response = new StableResponse();
        response.setId(stable.getId());
        response.setName(stable.getName());
        response.setPreferences(stable.getPreferences());
        
        String ownerName = membershipService.getStableOwnerUsername(stable.getId());
        response.setOwnerUsername(ownerName);

        List<Membership> allMemberships = stable.getMemberships();

        int memberCount = 0;
        for(Membership membership : allMemberships) {
            if(membership.getAcceptedInvite()) {
                memberCount++;
            }
        }

        response.setMemberCount(memberCount);
        response.setHorseCount(stable.getHorses().size());
        
        return response;
    }

    public StableResponse getStableById(Long stableId) {
        Optional<Stable> optStable = stableRepository.findById(stableId);

        if(optStable.isEmpty()) {
            throw new RuntimeException("No stable exists with id: " + stableId);
        }

        // Check if member of stable
        if(!membershipService.checkMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a member of this stable");
        }

        Stable stable = optStable.get();

        Membership userMembership = membershipService.getUserMembership(stableId);

        StableResponse response = mapToResponse(stable);
        response.setCurrentUserRole(userMembership.getMembershipRole().name());

        return response;
    }

    public List<StableResponse> getAllUserStables() {
        List<StableResponse> responses = new ArrayList<>();

        for(Membership membership : membershipService.getAllUserMemberships()) {
            if(membership.getAcceptedInvite()){
                Stable stable = membership.getStable();

                StableResponse response = mapToResponse(stable);
                response.setCurrentUserRole(membership.getMembershipRole().name());

                responses.add(response);
            }
        }

        return responses;
    }

    public StableResponse createStable(StableRequest request) {
        Stable stable = new Stable();
        stable.setPreferences(request.getPreferences());
        stable.setName(request.getName());
        Stable savedStable = stableRepository.save(stable); // Should give the stable an ID

        // Create OWNER membership
        membershipService.createOwnerMembership(savedStable.getId());

        return mapToResponse(savedStable);
    }

    public StableResponse updateStable(Long stableId, StableRequest request) {
        Optional<Stable> optStable = stableRepository.findById(stableId);

        if(optStable.isEmpty()) {
            throw new RuntimeException("No stable exists with id: " + stableId);
        }

        Stable stable = optStable.get();

        // Check if member of stable and owner/manager
        if(!membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a owner/staff or member of this stable");
        }

        if(request.getName() != null) stable.setName(request.getName());
        if(request.getPreferences() != null) stable.setPreferences(request.getPreferences());

        Stable updatedStable = stableRepository.save(stable);

        return mapToResponse(updatedStable);
    }

    public void deleteStable(Long stableId) {
        // Check if member of stable and owner/manager
        if(!membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a owner/staff or member of this stable");
        }

        stableRepository.deleteById(stableId);
    }
    
    public List<StableInviteGetResponse> getAllUserInvites() {
        List<StableInviteGetResponse> responses = new ArrayList<>();

        for(Membership membership : membershipService.getAllUserMemberships()) {
            if(!membership.getAcceptedInvite()) {
                StableInviteGetResponse response = new StableInviteGetResponse();
                
                Long stableId = membership.getStable().getId();
                response.setStableId(stableId);
                response.setStableName(membership.getStable().getName());
                response.setStableRole(membership.getMembershipRole());

                String name = membershipService.getStableOwnerUsername(stableId);
                response.setStableOwner(name);

                responses.add(response);
            }
        }

        return responses;
    }
    
    public void sendStableInvite(Long stableId, StableInviteRequest request) {
        // Find user by email
        // Create a membership for that user that is pending
        Optional<User> sendTo = userService.findUserByEmail(request.getEmail());
        Optional<Stable> optStable = stableRepository.findById(stableId);

        if(sendTo.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }
        if(optStable.isEmpty()) {
            throw new RuntimeException("Stable does not exist");
        }
        if(!membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a owner/staff or member of this stable");
        }


        User user = sendTo.get();
        Stable stable = optStable.get();
        MembershipRole role = request.getRole();

        membershipService.createJoinMembership(stable, user, role);
    }
    
    public void acceptStableInvite(Long stableId, StableAcceptRequest request) {
        Long currentUserId = currentUserService.getCurrentUser().getId();

        if(request.getAccepted()) {
            membershipService.updateAcceptMembership(currentUserId, stableId);
        } else {
            membershipService.deleteMembership(currentUserId, stableId);
        }
    }

    // public void sendStableInvite(Long stableId, StableInviteRequest request) {
    //     Optional<Stable> optStable = stableRepository.findById(stableId);

    //     if(optStable.isEmpty()) {
    //         throw new RuntimeException("No stable exists with id: " + stableId);
    //     }

    //     Stable stable = optStable.get();

    //     if(!membershipService.checkEditMembershipStatus(stableId)) {
    //         throw new RuntimeException("User is not a owner/staff or member of this stable");
    //     }

    //     String resetLink = frontendUrl + "/join-stable/?stableId=" + stable.getId();

    //     emailService.sendStableInviteEmail(request, resetLink, stable.getName());
    // }
}

package com.starace.stable_manager.scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.starace.stable_manager.dto.HorseAlert;
import com.starace.stable_manager.enums.AlertType;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.model.Stable;
import com.starace.stable_manager.repository.StableRepository;
import com.starace.stable_manager.service.EmailService;
import com.starace.stable_manager.service.MembershipService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HorseNotificationScheduler {
    private final StableRepository stableRepository;
    private final MembershipService membershipService;
    private final EmailService emailService;

    @Scheduled(cron = "@midnight")
    @Transactional
    public void dailyOverdueHorseCheck() {
        List<Stable> allStables = stableRepository.findAll();

        for(Stable stable : allStables) {

            if(stable.getPreferences() == null) {
                continue;
            }
            if(!stable.getPreferences().getEmailNotification()) {
                continue;
            }

            List<Horse> allHorses = stable.getHorses();
            Map<String, List<HorseAlert>> alertsByEmail = new HashMap<>();


            for(Horse horse : allHorses) {
                List<AlertType> alertsForHorse = checkAlert(horse, stable);

                if(!alertsForHorse.isEmpty()) {
                    String ownerEmail = membershipService.getStableOwnerEmail(stable.getId());
                    String horseName = horse.getName();

                    HorseAlert horseAlert = new HorseAlert();
                    horseAlert.setHorseName(horseName);
                    horseAlert.setAlerts(alertsForHorse);

                    alertsByEmail
                        .computeIfAbsent(ownerEmail, k -> new ArrayList<>())
                        .add(horseAlert);            
                }
            }

            for(Map.Entry<String, List<HorseAlert>> entry : alertsByEmail.entrySet()) {
                String email = entry.getKey();
                List<HorseAlert> alerts = entry.getValue();
                emailService.sendOverdueEmail(email, alerts, stable.getName());
            }
        }
    }

    private List<AlertType> checkAlert(Horse horse, Stable stable) {
        List<AlertType> alerts = new ArrayList<>();

        if(isCogginsOverdue(horse, stable.getPreferences().getOverdueCogginsDays())) {
            alerts.add(AlertType.COGGINS);
        }
        if(isFarrierOverdue(horse, stable.getPreferences().getOverdueFarrierDays())) {
            alerts.add(AlertType.FARRIER);
        }
        return alerts;
    }

    private boolean isCogginsOverdue(Horse horse, int days) {
        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.minusDays(days);

        if(horse.getLastCogginDate() == null) {
            return false;
        } else {
            return horse.getLastCogginDate().isBefore(cutoff);
        }
    }

    private boolean isFarrierOverdue(Horse horse, int days) {
        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.minusDays(days);

        if(horse.getLastFarrierDate() == null) {
            return false;
        } else {
            return horse.getLastFarrierDate().isBefore(cutoff);
        }
    }

}

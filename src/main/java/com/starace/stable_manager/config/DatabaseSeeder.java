package com.starace.stable_manager.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.starace.stable_manager.model.FeedingLog;
import com.starace.stable_manager.model.HealthRecord;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.model.Owner;
import com.starace.stable_manager.model.Stable;
import com.starace.stable_manager.model.Staff;
import com.starace.stable_manager.model.WaterLog;
import com.starace.stable_manager.repository.FeedingLogRepository;
import com.starace.stable_manager.repository.HealthRecordRepository;
import com.starace.stable_manager.repository.HorseRepository;
import com.starace.stable_manager.repository.OwnerRepository;
import com.starace.stable_manager.repository.StableRepository;
import com.starace.stable_manager.repository.StaffRepository;
import com.starace.stable_manager.repository.WaterLogRepository;

@Configuration
public class DatabaseSeeder {
    
    @Bean
    CommandLineRunner initDatabase(StableRepository stableRepository,
                                    HorseRepository horseRepository,
                                    StaffRepository staffRepository,
                                    HealthRecordRepository healthRecordRepository,
                                    FeedingLogRepository feedingLogRepository,
                                    WaterLogRepository waterLogRepository,
                                    OwnerRepository ownerRepository) {
        return args -> {
            if(stableRepository.count() == 0) {
                Stable stable = new Stable();
                stable.setAddress("123 Meadow Lane");
                Stable savedStable = stableRepository.save(stable);

                // Missing some attributes
                Staff staff = new Staff();
                staff.setName("John Smith");
                staff.setRole("Manager");
                staff.setUserEmail("JohnSmith@stable.com");
                staff.setContactInfo("555-0123");
                staff.setStable(savedStable);
                Staff savedStaff = staffRepository.save(staff);

                // Missing some attributes
                Horse horse = new Horse();
                horse.setName("Oguri Cap");
                horse.setNickname("Grey Ghost");
                horse.setBreed("Thoroughbred");
                horse.setBirthYear(1985);
                horse.setMicrochipId("MC-985001");
                horse.setIsMdBred(false);
                horse.setFoalingState("Hokkaido");
                horse.setLastCogginDate(LocalDate.now().minusMonths(6));
                horse.setLastFarrierDate(LocalDate.now().minusWeeks(1));
                horse.setMedicalNotes("Check front left shoe regularly.");
                horse.setStable(savedStable);
                Horse savedHorse = horseRepository.save(horse);

                FeedingLog feedingLog = new FeedingLog();
                feedingLog.setFeedType("Sweet Feed");
                feedingLog.setAmountLbs(4.5);
                feedingLog.setPurpose("Morning Grain");
                feedingLog.setFedAt(LocalDateTime.now());
                feedingLog.setHorse(savedHorse);
                feedingLog.setRecordedBy(savedStaff);
                feedingLogRepository.save(feedingLog);

                HealthRecord healthRecord = new HealthRecord();
                healthRecord.setTemperature(100.2);
                healthRecord.setWeight(1150.0);
                healthRecord.setHeartRate(38);
                healthRecord.setRespirationRate(12);
                healthRecord.setRecordedAt(LocalDateTime.now());
                healthRecord.setHorse(savedHorse);
                healthRecord.setRecordedBy(savedStaff);
                healthRecordRepository.save(healthRecord);

                WaterLog waterLog = new WaterLog();
                waterLog.setAmountLbs(15.0);
                waterLog.setPurpose("Post-workout hydration");
                waterLog.setRecordedAt(LocalDateTime.now());
                waterLog.setHorse(savedHorse);
                waterLog.setRecordedBy(savedStaff);
                waterLogRepository.save(waterLog);

                Owner owner = new Owner();
                owner.setEmergencyContact("555-0123");
                owner.setName("John Smith");
                owner.setPhoneNumber("555-0123");
                ownerRepository.save(owner);


                System.out.println("Successfully seeded database with test data.");
                System.out.println("Stable id: " + savedStable.getId());
                System.out.println("Horse id: " + savedHorse.getId());
                System.out.println("Staff id: " + savedStaff.getId());
            }
        };
    }
}

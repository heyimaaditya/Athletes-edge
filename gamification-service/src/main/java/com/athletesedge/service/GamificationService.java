package com.athletesedge.service;

import com.athletesedge.dto.GamificationUpdateDTO;
import com.athletesedge.model.AthleteGamificationProfile;
import com.athletesedge.repository.AthleteGamificationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GamificationService {
    @Autowired private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private AthleteGamificationProfileRepository profileRepo;

    public AthleteGamificationProfile selectAvatar(Long athleteId, String avatarType) {
        if (profileRepo.existsById(athleteId)) {
            throw new IllegalStateException("Avatar already selected.");
        }
        AthleteGamificationProfile profile = AthleteGamificationProfile.builder()
                .athleteId(athleteId)
                .avatarType(avatarType.toUpperCase())
                .level(1)
                .currentXp(0)
                .nextLevelXp(calculateNextLevelXp(1))
                .skillPoints(0)
                .build();
        return profileRepo.save(profile);
    }

    @Transactional
    public void addXp(Long athleteId, int xpToAdd) {
        AthleteGamificationProfile profile = profileRepo.findById(athleteId).orElse(null);
        if (profile == null) {
            System.out.println("Gamification profile not found for athlete " + athleteId + ". Cannot add XP.");
            return;
        }


        profile.setCurrentXp(profile.getCurrentXp() + xpToAdd);
        boolean leveledUp = false;
        while (profile.getCurrentXp() >= profile.getNextLevelXp()) {
            leveledUp = true;
            profile.setCurrentXp(profile.getCurrentXp() - profile.getNextLevelXp());
            profile.setLevel(profile.getLevel() + 1);
            profile.setNextLevelXp(calculateNextLevelXp(profile.getLevel()));
            profile.setSkillPoints(profile.getSkillPoints() + 1);
        }

        profileRepo.save(profile);
        

        if (leveledUp) {
            System.out.println("Athlete " + athleteId + " leveled up to level " + profile.getLevel() + "!");
        }
        GamificationUpdateDTO dto = GamificationUpdateDTO.builder()
        .athleteId(athleteId)
        .level(profile.getLevel())
        .currentXp(profile.getCurrentXp())
        .nextLevelXp(profile.getNextLevelXp())
        .skillPoints(profile.getSkillPoints())
        .leveledUp(leveledUp)
        .build();

    kafkaTemplate.send("gamification-updates", dto);
    }

    private int calculateNextLevelXp(int level) {
        return (int) (100 * Math.pow(level, 1.5));
    }

    public AthleteGamificationProfile getProfile(Long athleteId) {
        return profileRepo.findById(athleteId).orElse(null);
    }
}

package com.athletesedge.repository;

import com.athletesedge.model.AthleteGamificationProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthleteGamificationProfileRepository extends JpaRepository<AthleteGamificationProfile, Long> {
}

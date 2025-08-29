package com.athletesedge.repository;

import com.athletesedge.model.AthleteStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthleteStatsRepository extends JpaRepository<AthleteStats, Long> {

}
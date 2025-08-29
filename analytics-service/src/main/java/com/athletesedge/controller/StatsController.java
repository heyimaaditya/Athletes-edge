package com.athletesedge.controller;

import com.athletesedge.model.AthleteStats;
import com.athletesedge.repository.AthleteStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private AthleteStatsRepository athleteStatsRepository;

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<AthleteStats> getStatsByAthleteId(@PathVariable Long athleteId) {
        return athleteStatsRepository.findById(athleteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

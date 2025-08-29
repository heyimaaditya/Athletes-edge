package com.athletesedge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.athletesedge.model.Athlete;
import com.athletesedge.repository.AthleteRepository;
import com.athletesedge.security.JwtUtil;

@RestController
@RequestMapping("/api/users") 
public class AthleteController {

    @Autowired
    private AthleteRepository athleteRepository;

   @Autowired
    private JwtUtil jwtUtil;

    @PostMapping({"","/"})
    public Athlete createAthlete(@RequestBody Athlete athlete) {
        return athleteRepository.save(athlete);
    }

  
    @GetMapping({"","/"})
    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Athlete> getAthleteById(@PathVariable Long id) {
        return athleteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

   
    @PutMapping("/{id}")
    public ResponseEntity<Athlete> updateAthlete(@PathVariable Long id, @RequestBody Athlete athleteDetails) {
        return athleteRepository.findById(id)
                .map(athlete -> {
                    athlete.setName(athleteDetails.getName());
                    athlete.setAge(athleteDetails.getAge());
                    athlete.setSport(athleteDetails.getSport());
                    Athlete updatedAthlete = athleteRepository.save(athlete);
                    return ResponseEntity.ok(updatedAthlete);
                }).orElse(ResponseEntity.notFound().build());
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAthlete(@PathVariable Long id) {
        return athleteRepository.findById(id)
                .map(athlete -> {
                    athleteRepository.delete(athlete);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<Athlete> getCurrentUser(@RequestHeader("Authorization") String token) {
    
        String jwt = token.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(jwt);
        
        return athleteRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

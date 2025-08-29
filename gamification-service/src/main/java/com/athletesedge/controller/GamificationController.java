package com.athletesedge.controller;

import com.athletesedge.dto.SelectAvatarRequest;
import com.athletesedge.model.AthleteGamificationProfile;
import com.athletesedge.service.GamificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gamification")
public class GamificationController {

    @Autowired
    private GamificationService service;

    /**
     
     * @param athleteId The ID of the athlete.
     * @return The profile if found, otherwise 404 Not Found.
     */
    @GetMapping("/profile/{athleteId}")
    public ResponseEntity<AthleteGamificationProfile> getProfile(@PathVariable Long athleteId) {
        AthleteGamificationProfile profile = service.getProfile(athleteId);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     
     * @param athleteId The ID of the athlete.
     * @param request DTO containing the avatar type (e.g., "BEAR").
     * @return The newly created profile, or 409 Conflict if a profile already exists.
     */
    @PostMapping("/profile/{athleteId}/select-avatar")
    public ResponseEntity<AthleteGamificationProfile> selectAvatar(@PathVariable Long athleteId, @RequestBody SelectAvatarRequest request) {
        try {
            AthleteGamificationProfile profile = service.selectAvatar(athleteId, request.getAvatarType());
            return new ResponseEntity<>(profile, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
           
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}

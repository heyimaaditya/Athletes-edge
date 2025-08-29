package com.athletesedge.service;

import com.athletesedge.dto.AuthResponse;
import com.athletesedge.dto.LoginRequest;
import com.athletesedge.dto.RegisterRequest;
import com.athletesedge.model.Athlete;
import com.athletesedge.repository.AthleteRepository;
import com.athletesedge.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Athlete register(RegisterRequest request) {
        if (athleteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        Athlete athlete = Athlete.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) 
                .age(request.getAge())
                .sport(request.getSport())
                .build();
        
        return athleteRepository.save(athlete);
    }

    public AuthResponse login(LoginRequest request) {
        Athlete athlete = athleteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), athlete.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        String token = jwtUtil.generateToken(athlete);
        return new AuthResponse(token);
    }
}

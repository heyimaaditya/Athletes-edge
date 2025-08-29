package com.athletesedge.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AthleteProfileDto {
    private Long id;
    private String name;
    private String email;
    private String sport;
    private int age;
}

package com.athletesedge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AcwrDataPointDTO {
    private LocalDate date;
    private double acwr;
}
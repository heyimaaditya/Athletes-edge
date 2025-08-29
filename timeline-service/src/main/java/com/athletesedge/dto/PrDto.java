package com.athletesedge.dto;
import lombok.Data;
import java.time.LocalDate;
@Data public class PrDto { private LocalDate recordDate; private String exerciseName; private double value; private String unit; }

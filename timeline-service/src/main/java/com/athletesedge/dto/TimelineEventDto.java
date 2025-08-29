package com.athletesedge.dto;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class TimelineEventDto {
    private String title;
    private String cardTitle;
    private String cardSubtitle;
    private String cardDetailedText;
    private LocalDate date;
    private String type; // "PR", "GOAL", "WORKOUT", "MILESTONE"
}

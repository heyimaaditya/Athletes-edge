package com.athletesedge.service;

import com.athletesedge.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimelineService {

    private final WebClient.Builder webClientBuilder;

    public TimelineService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<List<TimelineEventDto>> getTimelineForAthlete(Long athleteId, String authToken) {
        Flux<PrDto> prsFlux = fetchPrs(athleteId, authToken);
        Flux<GoalDto> goalsFlux = fetchCompletedGoals(athleteId, authToken);
        Flux<WorkoutDto> workoutsFlux = fetchWorkouts(athleteId, authToken);

        return Flux.merge(
                prsFlux.map(this::transformPrToTimelineEvent),
                goalsFlux.map(this::transformGoalToTimelineEvent),
              
                workoutsFlux.collect(Collectors.groupingBy(WorkoutDto::getWorkoutDate))
                            .flatMapMany(map -> Flux.fromIterable(map.entrySet()))
                            .map(this::transformWorkoutsToTimelineEvent)
            )
            .collectList()
            .map(events -> {
                addMilestones(events, athleteId);
                // Final sort after adding milestones
                events.sort(Comparator.comparing(TimelineEventDto::getDate).reversed());
                return events;
            });
    }

    private Flux<PrDto> fetchPrs(Long athleteId, String authToken) {
        return webClientBuilder.build().get()
                .uri("http://PR-SERVICE/api/prs/athlete/{id}", athleteId)
                .header("Authorization", authToken)
                .retrieve()
                .bodyToFlux(PrDto.class)
                .onErrorResume(e -> Flux.empty());
    }

    private Flux<GoalDto> fetchCompletedGoals(Long athleteId, String authToken) {
        return webClientBuilder.build().get()
                .uri("http://GOAL-SERVICE/api/goals/athlete/{id}?status=COMPLETED", athleteId)
                .header("Authorization", authToken)
                .retrieve()
                .bodyToFlux(GoalDto.class)
                .onErrorResume(e -> Flux.empty());
    }

    private Flux<WorkoutDto> fetchWorkouts(Long athleteId, String authToken) {
        return webClientBuilder.build().get()
                .uri("http://WORKOUT-LOGGER-SERVICE/api/workouts/athlete/{id}", athleteId)
                .header("Authorization", authToken)
                .retrieve()
                .bodyToFlux(WorkoutDto.class)
                .onErrorResume(e -> Flux.empty());
    }

    private TimelineEventDto transformPrToTimelineEvent(PrDto pr) {
        return TimelineEventDto.builder()
                .date(pr.getRecordDate())
                .title("🏆 New Personal Record!")
                .cardTitle(pr.getExerciseName())
                .cardSubtitle(pr.getValue() + " " + pr.getUnit())
                .cardDetailedText("A new benchmark has been set. Incredible work!")
                .type("PR")
                .build();
    }

    private TimelineEventDto transformGoalToTimelineEvent(GoalDto goal) {
        return TimelineEventDto.builder()
                .date(goal.getEndDate())
                .title("🎯 Goal Achieved!")
                .cardTitle(goal.getDescription())
                .cardSubtitle("Mission Accomplished!")
                .cardDetailedText("You successfully completed one of your goals.")
                .type("GOAL")
                .build();
    }

    private TimelineEventDto transformWorkoutsToTimelineEvent(Map.Entry<LocalDate, List<WorkoutDto>> entry) {
        List<WorkoutDto> workouts = entry.getValue();
        int totalDuration = workouts.stream().mapToInt(WorkoutDto::getDurationMinutes).sum();
        String workoutTypes = workouts.stream().map(WorkoutDto::getType).distinct().collect(Collectors.joining(", "));
        
        return TimelineEventDto.builder()
                .date(entry.getKey())
                .title("🏃 Training Day")
                .cardTitle(workoutTypes)
                .cardSubtitle(totalDuration + " minutes of total work.")
                .cardDetailedText(workouts.size() + (workouts.size() > 1 ? " sessions" : " session") + " logged.")
                .type("WORKOUT")
                .build();
    }
    
    private void addMilestones(List<TimelineEventDto> events, Long athleteId) {
        long workoutCount = events.stream().filter(e -> "WORKOUT".equals(e.getType())).count();
        List<TimelineEventDto> milestones = new ArrayList<>();

        if (workoutCount >= 1) {
            LocalDate firstWorkoutDate = events.stream()
                .filter(e -> "WORKOUT".equals(e.getType()))
                .min(Comparator.comparing(TimelineEventDto::getDate))
                .get().getDate();
            milestones.add(TimelineEventDto.builder().date(firstWorkoutDate).title("The Journey Begins!").cardTitle("First Workout Logged").type("MILESTONE").build());
        }
        if (workoutCount >= 50) {
            LocalDate fiftiethWorkoutDate = events.stream()
                .filter(e -> "WORKOUT".equals(e.getType()))
                .sorted(Comparator.comparing(TimelineEventDto::getDate))
                .skip(49).findFirst().get().getDate();
            milestones.add(TimelineEventDto.builder().date(fiftiethWorkoutDate).title("50 Workouts Strong!").cardTitle("Consistency is Key").type("MILESTONE").build());
        }
      
        events.addAll(milestones);
    }
}
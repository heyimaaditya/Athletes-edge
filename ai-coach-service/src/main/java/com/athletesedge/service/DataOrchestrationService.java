package com.athletesedge.service;

import com.athletesedge.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

@Service
public class DataOrchestrationService {

    private final WebClient.Builder webClientBuilder;

    public DataOrchestrationService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     
     * @param athleteId 
     * @param authToken 
     * @return A Mono containing the aggregated DailyContext.
     */
    public Mono<DailyContext> getDailyContext(Long athleteId, String authToken) {
        // --- Define URLs for internal microservices ---
        String recoveryUrl = "http://RECOVERY-SERVICE/api/recovery/athlete/{id}/today";
        String plannerUrl = "http://PLANNER-SERVICE/api/planner/plan/athlete/{id}?weekStartDate={date}";
        String injuryUrl = "http://INJURY-PREDICTION-SERVICE/api/charts/acwr/{id}?days=1";

        // Calculate the start date of the current week (Monday)
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // --- Asynchronous API Calls in Parallel ---
        Mono<ReadinessDto> readinessMono = webClientBuilder.build().get()
                .uri(recoveryUrl, athleteId)
                .retrieve()
                .bodyToMono(ReadinessDto.class)
                .onErrorReturn(new ReadinessDto()); 

        Mono<PlannedWorkoutDto[]> planMono = webClientBuilder.build().get()
                .uri(plannerUrl, athleteId, startOfWeek)
                .retrieve()
                .bodyToMono(PlannedWorkoutDto[].class)
                .onErrorReturn(new PlannedWorkoutDto[0]); 

        Mono<AcwrDto[]> acwrMono = webClientBuilder.build().get()
                .uri(injuryUrl, athleteId)
                .retrieve()
                .bodyToMono(AcwrDto[].class)
                .onErrorReturn(new AcwrDto[0]); 

        // Combine the results of all parallel calls
        return Mono.zip(readinessMono, planMono, acwrMono)
            .map(tuple -> {
              
                ReadinessDto readiness = tuple.getT1();
                PlannedWorkoutDto[] weeklyPlan = tuple.getT2();
                AcwrDto[] acwrData = tuple.getT3();

                // Find today's specific workout from the weekly plan
                PlannedWorkoutDto todaysPlan = null;
                LocalDate today = LocalDate.now();
                if (weeklyPlan != null && weeklyPlan.length > 0) {
                    for (PlannedWorkoutDto workout : weeklyPlan) {
                        if (workout.getPlannedDate() != null && workout.getPlannedDate().equals(today)) {
                            todaysPlan = workout;
                            break;
                        }
                    }
                }

                // Build the final context object
                return DailyContext.builder()
                    .readiness(readiness)
                    .plan(todaysPlan)
                    .acwr(acwrData.length > 0 ? acwrData[0] : null)
                    .build();
            });
    }

    /**
     
     * @param athleteId 
     * @param authToken
     * @return 
     */
    public Mono<PlanningContext> getAthleteContextForPlanning(Long athleteId, String authToken) {
        String userUrl = "http://USER-SERVICE/api/users/{id}";
        String statsUrl = "http://ANALYTICS-SERVICE/api/stats/athlete/{id}";

        Mono<AthleteProfileDto> profileMono = webClientBuilder.build().get()
                .uri(userUrl, athleteId)
                .header("Authorization", authToken) 
                .retrieve()
                .bodyToMono(AthleteProfileDto.class);

        Mono<AthleteStatsDto> statsMono = webClientBuilder.build().get()
                .uri(statsUrl, athleteId)
                .retrieve()
                .bodyToMono(AthleteStatsDto.class)
                .onErrorReturn(new AthleteStatsDto()); 

        return Mono.zip(profileMono, statsMono)
                .map(tuple -> PlanningContext.builder()
                        .profile(tuple.getT1())
                        .stats(tuple.getT2())
                        .build());
    }
    
    /**
     
     * @param plan 
     * @param authToken 
     * @return 
     */
    public Mono<Void> saveGeneratedPlan(GeneratedPlanDto plan, String authToken) {
        String plannerUrl = "http://PLANNER-SERVICE/api/planner/plan/bulk";
        
        return webClientBuilder.build().post()
                .uri(plannerUrl)
                .header("Authorization", authToken)
                .bodyValue(plan)
                .retrieve()
                .toBodilessEntity()
                .then(); 
    }
}
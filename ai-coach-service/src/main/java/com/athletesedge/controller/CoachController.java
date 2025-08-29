package com.athletesedge.controller;

import com.athletesedge.dto.ChatRequest;
import com.athletesedge.dto.CoachResponse;
import com.athletesedge.service.DataOrchestrationService;
import com.athletesedge.service.GroqService;

import java.time.LocalDate;
import org.springframework.cache.annotation.CacheEvict; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/coach")
public class CoachController {

    @Autowired
    private DataOrchestrationService orchestrationService;

    @Autowired
    private GroqService groqService;

    
    @GetMapping("/today/{athleteId}")
    public Mono<CoachResponse> getTodaysTip(
        @PathVariable Long athleteId,
        @RequestHeader("Authorization") String authToken) { 

        return orchestrationService.getDailyContext(athleteId, authToken)
            .flatMap(context -> groqService.getCoachingTip(athleteId, context));
    }

  
    @PostMapping(value = "/chat/{athleteId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithCoach(
        @PathVariable Long athleteId,
        @RequestBody ChatRequest chatRequest,
        @RequestHeader("Authorization") String authToken) { 

        return orchestrationService.getDailyContext(athleteId, authToken) 
            .flatMapMany(context -> groqService.getStreamingChatResponse(context, chatRequest.getMessages()));
    }

   
    @PostMapping("/generate-plan/{athleteId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> generateWeeklyPlan(
        @PathVariable Long athleteId,
        @RequestHeader("Authorization") String authToken) { 
        
        

        return orchestrationService.getAthleteContextForPlanning(athleteId, authToken) 
            .flatMap(context -> groqService.generatePlan(context))
            .flatMap(plan -> {
   
        plan.setAthleteId(athleteId); 
                plan.setWeekStartDate(LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)));
                return orchestrationService.saveGeneratedPlan(plan, authToken);
            });
    }
    @PostMapping("/clear-cache/{athleteId}")
    @CacheEvict(value = "coachTips", key = "#athleteId + T(java.time.LocalDate).now().toString()")
    public ResponseEntity<Void> clearTodaysTipCache(@PathVariable Long athleteId) {
        System.out.println("Cache cleared for athlete: " + athleteId + " for today.");
        return ResponseEntity.ok().build();
    }
}

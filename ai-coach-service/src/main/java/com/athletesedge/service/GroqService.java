package com.athletesedge.service;

import com.athletesedge.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper(); // For parsing JSON strings

    @Value("${groq.api.url}")
    private String groqUrl;

    @Value("${groq.api.key}")
    private String groqApiKey;

    // --- Method 1: Get Daily Coaching Tip (Non-streaming) ---
    @Cacheable(value = "coachTips", key = "#athleteId + T(java.time.LocalDate).now().toString()")
    public Mono<CoachResponse> getCoachingTip(Long athleteId, DailyContext context) {
        String prompt = buildDailyTipPrompt(context);

        var requestBody = Map.of(
            "model", "llama3-8b-8192",
            "messages", new Object[]{
                Map.of("role", "system", "content", "You are an elite sports performance and recovery coach named 'Edge'. Your advice is concise, actionable, and encouraging. Always return your response as a valid JSON object with three keys: 'title' (a short, catchy headline), 'signal' (one of: GREEN, YELLOW, RED), and 'recommendation' (a 2-3 sentence paragraph)."),
                Map.of("role", "user", "content", prompt)
            },
            "response_format", Map.of("type", "json_object")
        );

        return webClient.post()
            .uri(groqUrl)
            .header("Authorization", "Bearer " + groqApiKey)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(jsonNode -> {
                String content = jsonNode.at("/choices/0/message/content").asText();
                try {
                    return objectMapper.readValue(content, CoachResponse.class);
                } catch (Exception e) {
                    System.err.println("Failed to parse CoachResponse from Groq: " + content);
                    throw new RuntimeException("Error parsing coach response from Groq", e);
                }
            });
    }

    // --- Method 2: Get Streaming Chat Response ---
    public Flux<String> getStreamingChatResponse(DailyContext context, List<ChatMessage> history) {
        String systemPrompt = buildSystemPromptForChat(context);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        history.forEach(msg -> messages.add(Map.of("role", msg.getRole(), "content", msg.getContent())));

        var requestBody = Map.of(
            "model", "llama3-8b-8192",
            "messages", messages,
            "stream", true
        );

        return webClient.post()
            .uri(groqUrl)
            .header("Authorization", "Bearer " + groqApiKey)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToFlux(String.class)
            .mapNotNull(line -> {
                if (line.startsWith("data: ")) {
                    String json = line.substring(6);
                    if ("[DONE]".equals(json.trim())) {
                        return null;
                    }
                    try {
                        JsonNode chunk = objectMapper.readTree(json);
                        JsonNode delta = chunk.at("/choices/0/delta/content");
                        return delta.isMissingNode() ? "" : delta.asText();
                    } catch (Exception e) {
                        return "";
                    }
                }
                return "";
            });
    }

    // --- Method 3: Generate Weekly Plan (Non-streaming) ---
    public Mono<GeneratedPlanDto> generatePlan(PlanningContext context) {
        String prompt = buildPlanningPrompt(context);
        
        var requestBody = Map.of(
            "model", "llama3-70b-8192", 
            "messages", new Object[]{
                Map.of("role", "system", "content", "You are a world-class strength and conditioning coach. You create weekly workout plans based on an athlete's profile and history. You ALWAYS return a valid JSON object. The JSON should be an object with a single key 'weeklyPlan', which is an array of objects. Each object in the array represents a day and must have three keys: 'dayOfWeek' (an integer from 1 for Monday to 7 for Sunday), 'exerciseName' (e.g., 'Upper Body Strength'), and 'description' (a concise string with details of the workout). Include 1-2 rest days in the plan."),
                Map.of("role", "user", "content", prompt)
            },
            "response_format", Map.of("type", "json_object")
        );

      
        System.out.println("--- Sending Plan Generation Request to Groq ---");
        System.out.println("Prompt: " + prompt);
        
        return webClient.post()
            .uri(groqUrl)
            .header("Authorization", "Bearer " + groqApiKey)
            .bodyValue(requestBody) 
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(jsonNode -> {
                String content = jsonNode.at("/choices/0/message/content").asText();
                try {
                   
                    return objectMapper.readValue(content, GeneratedPlanDto.class);
                } catch (Exception e) { 
                    System.err.println("Failed to parse GeneratedPlanDto from Groq: " + content);
                    throw new RuntimeException("Error parsing generated plan from Groq", e);
                }
            });
    }

    // --- Helper Methods for Building Prompts ---

    private String buildDailyTipPrompt(DailyContext context) {
        if (context.getReadiness() == null || context.getReadiness().getReadinessScore() == 0) {
            return "The athlete has not logged their readiness score for today. Ask them to log it to get a personalized coaching tip.";
        }
        
        StringBuilder sb = new StringBuilder("Here is my data for today:\n");
        sb.append("- Readiness Score: ").append(context.getReadiness().getReadinessScore()).append("/100\n");
        if (context.getAcwr() != null && context.getAcwr().getAcwr() > 0) {
            sb.append("- Recent Training Load (ACWR): ").append(String.format("%.2f", context.getAcwr().getAcwr())).append("\n");
        }
        if (context.getPlan() != null) {
            sb.append("- Planned Workout: ").append(context.getPlan().getExerciseName()).append(" (").append(context.getPlan().getDescription()).append(")\n");
        } else {
            sb.append("- Planned Workout: Rest day or not planned.\n");
        }
        sb.append("\nBased on this data, provide your coaching recommendation for today in the specified JSON format.");
        return sb.toString();
    }
    
    private String buildSystemPromptForChat(DailyContext context) {
        StringBuilder sb = new StringBuilder("You are an elite, encouraging sports performance AI coach named 'Edge'. Your athlete is talking to you. Use the following real-time data to inform your answers. Keep your responses concise and helpful.\n\nCURRENT ATHLETE DATA:\n");
        if (context.getReadiness() != null && context.getReadiness().getReadinessScore() > 0) {
            sb.append("- Today's Readiness Score: ").append(context.getReadiness().getReadinessScore()).append("/100\n");
        } else {
            sb.append("- Today's Readiness: Not logged yet.\n");
        }
        if (context.getAcwr() != null && context.getAcwr().getAcwr() > 0) {
            sb.append("- Recent Training Load (ACWR): ").append(String.format("%.2f", context.getAcwr().getAcwr())).append("\n");
        }
        if (context.getPlan() != null) {
            sb.append("- Today's Planned Workout: ").append(context.getPlan().getExerciseName()).append("\n");
        } else {
            sb.append("- Today's Plan: Rest day.\n");
        }
        return sb.toString();
    }
    
    private String buildPlanningPrompt(PlanningContext context) {
       
        String primaryGoal = "Muscle Gain"; 
        
       
        String sport = "General Fitness";
        if (context.getProfile() != null && context.getProfile().getSport() != null && !context.getProfile().getSport().isEmpty()) {
            sport = context.getProfile().getSport();
        }

        String experienceLevel = "Beginner";
        int workoutCount = 0;
        if (context.getStats() != null) {
            workoutCount = context.getStats().getWorkoutCount();
            if (workoutCount > 50) {
                experienceLevel = "Intermediate";
            } else if (workoutCount > 20) {
                experienceLevel = "Novice";
            }
        }
        
        return String.format(
            "Generate a 7-day workout plan for the upcoming week for the following athlete:\n" +
            "- Primary Sport: %s\n" +
            "- Experience Level: %s (based on %d logged workouts)\n" +
            "- Primary Goal for this cycle: %s\n\n" +
            "The plan should be tailored to their sport and experience. Follow principles of progressive overload and include adequate rest. Provide the plan in the specified JSON format.",
            sport,
            experienceLevel,
            workoutCount,
            primaryGoal
        );
    }
}

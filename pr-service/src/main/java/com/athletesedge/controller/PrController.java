package com.athletesedge.controller;

import com.athletesedge.dto.CreatePrRequest;
import com.athletesedge.dto.PrLoggedEventDTO;
import com.athletesedge.model.PersonalRecord;
import com.athletesedge.repository.PersonalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/prs")
public class PrController {
    @Autowired
    private PersonalRecordRepository repository;
     @Autowired
    private KafkaTemplate<String, PrLoggedEventDTO> kafkaTemplate;
    private static final String KAFKA_TOPIC = "pr-events";
    @PostMapping
    public ResponseEntity<PersonalRecord> createPr(@RequestBody CreatePrRequest request) {
        PersonalRecord pr = PersonalRecord.builder()
                .athleteId(request.getAthleteId())
                .exerciseName(request.getExerciseName())
                .value(request.getValue())
                .unit(request.getUnit())
                .recordDate(request.getRecordDate())
                .build();
         PersonalRecord savedPr = repository.save(pr);

       
        PrLoggedEventDTO event = new PrLoggedEventDTO(savedPr.getAthleteId(), savedPr.getExerciseName());
        kafkaTemplate.send(KAFKA_TOPIC, String.valueOf(savedPr.getAthleteId()), event);
        System.out.println("PR Logged event sent to Kafka: " + event);
        return new ResponseEntity<>(repository.save(pr), HttpStatus.CREATED);
    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<PersonalRecord>> getPrsByAthleteId(@PathVariable Long athleteId) {
        return ResponseEntity.ok(repository.findByAthleteIdOrderByRecordDateDesc(athleteId));
    }
}
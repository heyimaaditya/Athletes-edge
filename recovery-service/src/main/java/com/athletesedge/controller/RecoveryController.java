package com.athletesedge.controller;

import com.athletesedge.dto.LogRecoveryRequest;
import com.athletesedge.model.RecoveryLog;
import com.athletesedge.service.RecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/recovery")
public class RecoveryController {

    @Autowired
    private RecoveryService service;

    @PostMapping
    public ResponseEntity<RecoveryLog> logRecovery(@RequestBody LogRecoveryRequest request) {
        RecoveryLog savedLog = service.logOrUpdateRecovery(request);
        return ResponseEntity.ok(savedLog);
    }

    @GetMapping("/athlete/{athleteId}/today")
    public ResponseEntity<RecoveryLog> getTodaysLog(@PathVariable Long athleteId) {
        Optional<RecoveryLog> log = service.getTodaysRecoveryLog(athleteId);
        return log.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
}

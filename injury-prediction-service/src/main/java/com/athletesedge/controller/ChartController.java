package com.athletesedge.controller;

import com.athletesedge.dto.AcwrDataPointDTO;
import com.athletesedge.service.AcwrCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charts")
public class ChartController {

    @Autowired
    private AcwrCalculationService acwrService;

    @GetMapping("/acwr/{athleteId}")
    public ResponseEntity<List<AcwrDataPointDTO>> getAcwrHistory(
        @PathVariable Long athleteId,
        @RequestParam(defaultValue = "60") int days) {
            List<AcwrDataPointDTO> history = acwrService.getAcwrHistory(athleteId, days);
            return ResponseEntity.ok(history);
    }
}

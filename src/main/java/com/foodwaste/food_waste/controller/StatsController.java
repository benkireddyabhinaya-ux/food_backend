package com.foodwaste.food_waste.controller;

import com.foodwaste.food_waste.dto.ImpactStatsDto;
import com.foodwaste.food_waste.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/stats", "/stats"})
@Tag(name = "Analytics & Impact", description = "Real-time statistics on food rescued, meals distributed, and carbon saved")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/summary")
    @Operation(summary = "Get platform-wide impact statistics and metrics")
    public ImpactStatsDto getSummary() {
        return statsService.getImpactSummary();
    }
}

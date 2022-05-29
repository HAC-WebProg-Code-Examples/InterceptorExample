package com.example.interceptorexample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Controller
public class StatsController {

    private final StatisticsService stats;

    public StatsController(StatisticsService stats) {
        this.stats = stats;
    }

    @GetMapping("/stats")
    public String getStats(Model model) {
        HashMap<String, Double> currentStats = stats.getStats();
        model.addAllAttributes(currentStats);
        return "stats";
    }
}

package uy.edu.taller.sige.geo_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uy.edu.taller.sige.geo_api.dto.stats.StatsResponse;
import uy.edu.taller.sige.geo_api.service.statistics.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/api/gis")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/stats")
    public List<StatsResponse> getAllStats() {
        return statisticsService.getAllProviderStats();
    }
}

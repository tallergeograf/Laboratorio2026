package uy.edu.taller.sige.geo_api.service.statistics;

import java.util.List;

import uy.edu.taller.sige.geo_api.dto.stats.StatsRequest;
import uy.edu.taller.sige.geo_api.dto.stats.StatsResponse;

public interface StatisticsService {
    List<StatsResponse> getAllProviderStats(StatsRequest request);
}

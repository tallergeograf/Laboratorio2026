package uy.edu.taller.sige.geo_api.dto.stats;

import java.util.List;

public record StatsResponse(
    String provider,
    int sampleSize,
    StatsAccuracyResponse accuracyStats,
    StatsCoverageResponse coverageStats,
    StatsReliabilityResponse reliabilityStats,
    List<StatsAddressResponse> addresses
) {}

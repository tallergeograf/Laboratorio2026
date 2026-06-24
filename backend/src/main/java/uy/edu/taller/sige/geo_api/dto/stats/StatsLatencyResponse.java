package uy.edu.taller.sige.geo_api.dto.stats;

public record StatsLatencyResponse(
    double averageLatencyMs,
    double medianLatencyMs,
    double maxLatencyMs
) {
}

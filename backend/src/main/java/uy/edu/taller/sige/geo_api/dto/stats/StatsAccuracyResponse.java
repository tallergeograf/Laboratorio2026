package uy.edu.taller.sige.geo_api.dto.stats;

public record StatsAccuracyResponse(
    double averageError,
    double maxError,
    double medianError,
    double percentageWithinMeters
) {

}

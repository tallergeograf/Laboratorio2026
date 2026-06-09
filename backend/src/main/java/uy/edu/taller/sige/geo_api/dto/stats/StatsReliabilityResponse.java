package uy.edu.taller.sige.geo_api.dto.stats;

public record StatsReliabilityResponse(
    int totalErrorsTypographic,
    int totalErrorsPermutation,
    int totalErrorsRural,
    int totalErrorsUrban
) {

}

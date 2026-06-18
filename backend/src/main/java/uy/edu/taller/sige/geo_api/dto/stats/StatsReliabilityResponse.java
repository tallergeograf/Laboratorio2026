package uy.edu.taller.sige.geo_api.dto.stats;

public record StatsReliabilityResponse(
    int totalErrorsTypographic,
    int totalTypographic,
    int totalErrorsPermutation,
    int totalPermutation,
    int totalErrorsAbbreviation,
    int totalAbbreviation,
    int totalErrorsComun,
    int totalComun,
    int totalErrorsRural,
    int totalRural,
    int totalErrorsUrban,
    int totalUrban
) {
}

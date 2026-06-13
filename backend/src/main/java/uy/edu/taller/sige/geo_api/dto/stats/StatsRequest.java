package uy.edu.taller.sige.geo_api.dto.stats;

public record StatsRequest(
    StatsFilterRequest filters,
    Boolean demo
) {}

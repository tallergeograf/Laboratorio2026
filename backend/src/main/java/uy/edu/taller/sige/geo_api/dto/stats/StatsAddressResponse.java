package uy.edu.taller.sige.geo_api.dto.stats;

public record StatsAddressResponse(
    Long addressId,
    String addressText,
    Double realLat,
    Double realLon,
    Double geocoderLat,
    Double geocoderLon,
    double errorMeters
) {}

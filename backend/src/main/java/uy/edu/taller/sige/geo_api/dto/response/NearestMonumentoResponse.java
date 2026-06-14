package uy.edu.taller.sige.geo_api.dto.response;

public record NearestMonumentoResponse(
    Long id,
    Long osmId,
    String name,
    Double lat,
    Double lon,
    String street,
    String city,
    Double distanceMeters,
    Double queryLat,
    Double queryLon
) {}

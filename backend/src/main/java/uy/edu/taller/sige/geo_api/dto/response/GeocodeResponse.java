package uy.edu.taller.sige.geo_api.dto.response;

public record GeocodeResponse(
    String displayName,
    Double lat,
    Double lon,
    String street,
    String houseNumber,
    String city,
    String state,
    String country,
    String countryCode,
    String postcode,
    String source,
    String neighbourhood,
    Double confidence,
    Boolean approximate,
    Double latencyMs
) {}

package uy.edu.taller.sige.geo_api.dto.request;

public record GeocodeRequestSearch(
    String query,
    Integer limit,
    String street,
    String houseNumber,
    String city,
    String state
) {}

package uy.edu.taller.sige.geo_api.client;

import java.util.List;

import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;

public record GeocoderSearchResult(
    List<GeocodeResponse> results,
    int statusCode
) {
    public static GeocoderSearchResult ok(List<GeocodeResponse> results) {
        return new GeocoderSearchResult(results, 200);
    }

    public static GeocoderSearchResult error(int statusCode) {
        return new GeocoderSearchResult(List.of(), statusCode);
    }
}

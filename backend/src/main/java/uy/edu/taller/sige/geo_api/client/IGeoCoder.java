package uy.edu.taller.sige.geo_api.client;

import java.util.List;

import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;

public interface IGeoCoder {
    List<GeocoderSearchResult> searchBatch(List<GeocodeRequestSearch> requests);
    List<GeocodeResponse> reverse(GeocodeRequestReverse request);
}

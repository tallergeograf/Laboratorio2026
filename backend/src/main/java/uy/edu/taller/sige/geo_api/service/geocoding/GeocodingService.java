package uy.edu.taller.sige.geo_api.service.geocoding;

import java.util.List;

import uy.edu.taller.sige.geo_api.model.enums.GeocoderProvider;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;

public interface GeocodingService {
    List<GeocodeResponse> search(GeocoderProvider provider, GeocodeRequestSearch request);
    List<GeocodeResponse> reverse(GeocoderProvider provider, GeocodeRequestReverse request);
}
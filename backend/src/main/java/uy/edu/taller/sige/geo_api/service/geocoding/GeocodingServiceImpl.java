package uy.edu.taller.sige.geo_api.service.geocoding;

import java.util.List;

import org.springframework.stereotype.Service;

import uy.edu.taller.sige.geo_api.client.GeoCoderFactory;
import uy.edu.taller.sige.geo_api.client.GeocoderSearchResult;
import uy.edu.taller.sige.geo_api.model.enums.GeocoderProvider;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    private final GeoCoderFactory factory;

    public GeocodingServiceImpl(GeoCoderFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<GeocodeResponse> search(GeocoderProvider provider, GeocodeRequestSearch request) {
        List<GeocoderSearchResult> results = factory.getGeoCoder(provider).searchBatch(List.of(request));
        return results.isEmpty() ? List.of() : results.get(0).results();
    }

    @Override
    public List<GeocodeResponse> reverse(GeocoderProvider provider, GeocodeRequestReverse request) {
        return factory.getGeoCoder(provider).reverse(request);
    }
}
package uy.edu.taller.sige.geo_api.service.geocoding;

import java.util.List;

import org.springframework.stereotype.Service;

import uy.edu.taller.sige.geo_api.client.GeoCoderFactory;
import uy.edu.taller.sige.geo_api.enums.GeocoderProvider;
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
        return factory.getGeoCoder(provider).search(request);
    }

    @Override
    public List<GeocodeResponse> reverse(GeocoderProvider provider, GeocodeRequestReverse request) {
        return factory.getGeoCoder(provider).reverse(request);
    }
}
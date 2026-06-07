package uy.edu.taller.sige.geo_api.client.nominatim;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.client.IGeoCoder;
import uy.edu.taller.sige.geo_api.client.properties.NominatimProperties;
import uy.edu.taller.sige.geo_api.dto.nominatim.response.NominatimPlaceDTO;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;
import uy.edu.taller.sige.geo_api.utils.RestClient;
import uy.edu.taller.sige.geo_api.utils.UrlBuilder;

@Component
public class NominatimClient implements IGeoCoder {

    private final RestClient restClient;
    private final NominatimProperties properties;
    private final NominatimMapper mapper;

    private final HttpHeaders headers;

    public NominatimClient(RestClient restClient, NominatimProperties properties, NominatimMapper mapper) {
        this.restClient = restClient;
        this.properties = properties;
        this.mapper = mapper;
        this.headers = new HttpHeaders();
        this.headers.set(HttpHeaders.USER_AGENT, properties.getUserAgent());
    }

    @Override
    public List<GeocodeResponse> search(GeocodeRequestSearch request) {
        String url = new UrlBuilder()
            .baseUrl(properties.getBaseUrl())
            .path(properties.getEndpoints().getSearch())
            .params(mapper.toNominatimSearchParamsDTO(request).toMap())
            .build();
        return mapper.toGeocodeResponseList(restClient.get(url, new ParameterizedTypeReference<List<NominatimPlaceDTO>>() {}, headers));
    }

    @Override
    public List<GeocodeResponse> reverse(GeocodeRequestReverse request) {
        String url = new UrlBuilder()
            .baseUrl(properties.getBaseUrl())
            .path(properties.getEndpoints().getReverse())
            .params(mapper.toNominatimReverseParamsDTO(request).toMap())
            .build();
        return List.of(mapper.toGeocodeResponse(restClient.get(url, NominatimPlaceDTO.class, headers)));
    }
}

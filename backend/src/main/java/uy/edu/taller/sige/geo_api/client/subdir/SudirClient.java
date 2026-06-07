package uy.edu.taller.sige.geo_api.client.subdir;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.client.IGeoCoder;
import uy.edu.taller.sige.geo_api.client.properties.SudirProperties;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;
import uy.edu.taller.sige.geo_api.dto.sudir.response.SudirGeocodeResultDTO;
import uy.edu.taller.sige.geo_api.dto.sudir.response.SudirReverseResultDTO;
import uy.edu.taller.sige.geo_api.utils.RestClient;
import uy.edu.taller.sige.geo_api.utils.UrlBuilder;

@Component
public class SudirClient implements IGeoCoder {

    private final RestClient restClient;
    private final SudirProperties properties;
    private final SudirMapper mapper;

    private final HttpHeaders headers;

    public SudirClient(RestClient restClient, SudirProperties properties, SudirMapper mapper) {
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
            .params(mapper.toSudirGeocodeParamsDTO(request).toMap())
            .build();
        return mapper.fromGeocodeList(restClient.get(url, new ParameterizedTypeReference<List<SudirGeocodeResultDTO>>() {}, headers));
    }

    @Override
    public List<GeocodeResponse> reverse(GeocodeRequestReverse request) {
        String url = new UrlBuilder()
            .baseUrl(properties.getBaseUrl())
            .path(properties.getEndpoints().getReverse())
            .params(mapper.toSudirReverseParamsDTO(request).toMap())
            .build();
        return mapper.fromReverseList(restClient.get(url, new ParameterizedTypeReference<List<SudirReverseResultDTO>>() {}, headers));
    }
}

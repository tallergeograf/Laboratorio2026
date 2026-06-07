package uy.edu.taller.sige.geo_api.client.photon;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.client.IGeoCoder;
import uy.edu.taller.sige.geo_api.client.properties.PhotonProperties;
import uy.edu.taller.sige.geo_api.dto.photon.request.PhotonReverseParamsDTO;
import uy.edu.taller.sige.geo_api.dto.photon.request.PhotonSearchParamsDTO;
import uy.edu.taller.sige.geo_api.dto.photon.response.PhotonResponseDTO;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;
import uy.edu.taller.sige.geo_api.utils.RestClient;
import uy.edu.taller.sige.geo_api.utils.UrlBuilder;

@Component
public class PhotonClient implements IGeoCoder {

    private final RestClient restClient;
    private final PhotonProperties properties;
    private final PhotonMapper mapper;

    private final HttpHeaders headers;

    public PhotonClient(RestClient restClient, PhotonProperties properties, PhotonMapper mapper) {
        this.restClient = restClient;
        this.properties = properties;
        this.mapper = mapper;
        this.headers = new HttpHeaders();
        this.headers.set(HttpHeaders.USER_AGENT, properties.getUserAgent());
    }

    @Override
    public List<GeocodeResponse> search(GeocodeRequestSearch request) {
        PhotonSearchParamsDTO searchParams = mapper.toPhotonSearchParamsDTO(request);

        String url = new UrlBuilder()
            .baseUrl(properties.getBaseUrl())
            .path(properties.getEndpoints().getSearch())
            .params(searchParams.toMap())
            .build();

        return mapper.toGeocodeResponseList(restClient.get(url, PhotonResponseDTO.class, headers));
    }

    @Override
    public List<GeocodeResponse> reverse(GeocodeRequestReverse request) {
        PhotonReverseParamsDTO reverseParams = mapper.toPhotonReverseParamsDTO(request);

        String url = new UrlBuilder()
            .baseUrl(properties.getBaseUrl())
            .path(properties.getEndpoints().getReverse())
            .params(reverseParams.toMap())
            .build();

        return mapper.toGeocodeResponseList(restClient.get(url, PhotonResponseDTO.class, headers));
    }
}

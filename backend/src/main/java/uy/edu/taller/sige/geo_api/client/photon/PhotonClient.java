package uy.edu.taller.sige.geo_api.client.photon;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import uy.edu.taller.sige.geo_api.client.GeocoderSearchResult;
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

    private static final Logger log = LoggerFactory.getLogger(PhotonClient.class);
    private static final int BETWEEN_REQUESTS_MS = 1000;
    private static final int RETRY_WAIT_MS = 5000;

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
    public List<GeocoderSearchResult> searchBatch(List<GeocodeRequestSearch> requests) {
        List<GeocoderSearchResult> results = new ArrayList<>();
        for (GeocodeRequestSearch request : requests) {
            results.add(searchOne(request));
            sleep(BETWEEN_REQUESTS_MS);
        }
        return results;
    }

    private GeocoderSearchResult searchOne(GeocodeRequestSearch request) {
        try {
            String url = buildSearchUrl(request);
            long start = System.nanoTime();
            PhotonResponseDTO raw = restClient.get(url, PhotonResponseDTO.class, headers);
            double latencyMs = (System.nanoTime() - start) / 1_000_000.0;
            return GeocoderSearchResult.ok(mapper.toGeocodeResponseList(raw, latencyMs));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 429) {
                log.warn("Photon rate limited for '{}', retrying after {}ms", request.full_address(), RETRY_WAIT_MS);
                sleep(RETRY_WAIT_MS);
                try {
                    String url = buildSearchUrl(request);
                    long start = System.nanoTime();
                    PhotonResponseDTO raw = restClient.get(url, PhotonResponseDTO.class, headers);
                    double latencyMs = (System.nanoTime() - start) / 1_000_000.0;
                    return GeocoderSearchResult.ok(mapper.toGeocodeResponseList(raw, latencyMs));
                } catch (Exception retryEx) {
                    log.warn("Photon retry failed for '{}': {}", request.full_address(), retryEx.getMessage());
                    return GeocoderSearchResult.error(429);
                }
            }
            log.warn("Photon HTTP {} for '{}'", e.getStatusCode().value(), request.full_address());
            return GeocoderSearchResult.error(e.getStatusCode().value());
        } catch (Exception e) {
            log.warn("Photon error for '{}': {}", request.full_address(), e.getMessage());
            return GeocoderSearchResult.error(0);
        }
    }

    private String buildSearchUrl(GeocodeRequestSearch request) {
        PhotonSearchParamsDTO params = mapper.toPhotonSearchParamsDTO(request);
        return new UrlBuilder()
            .baseUrl(properties.getBaseUrl())
            .path(properties.getEndpoints().getSearch())
            .params(params.toMap())
            .rawParams(params.toRawMap())
            .multiParams(params.toMultiMap())
            .build();
    }

    @Override
    public List<GeocodeResponse> reverse(GeocodeRequestReverse request) {
        PhotonReverseParamsDTO reverseParams = mapper.toPhotonReverseParamsDTO(request);
        String url = new UrlBuilder()
            .baseUrl(properties.getBaseUrl())
            .path(properties.getEndpoints().getReverse())
            .params(reverseParams.toMap())
            .multiParams(reverseParams.toMultiMap())
            .build();
        long start = System.nanoTime();
        PhotonResponseDTO raw = restClient.get(url, PhotonResponseDTO.class, headers);
        double latencyMs = (System.nanoTime() - start) / 1_000_000.0;
        return mapper.toGeocodeResponseList(raw, latencyMs);
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}

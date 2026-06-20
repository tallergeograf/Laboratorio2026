package uy.edu.taller.sige.geo_api.client.sudir;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import uy.edu.taller.sige.geo_api.client.GeocoderSearchResult;
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

    private static final Logger log = LoggerFactory.getLogger(SudirClient.class);
    private static final int RETRY_WAIT_MS = 5000;

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
    public List<GeocoderSearchResult> searchBatch(List<GeocodeRequestSearch> requests) {
        List<GeocoderSearchResult> results = new ArrayList<>();
        for (GeocodeRequestSearch request : requests) {
            results.add(searchOne(request));
        }
        return results;
    }

    private GeocoderSearchResult searchOne(GeocodeRequestSearch request) {
        try {
            String url = buildSearchUrl(request);
            long start = System.nanoTime();
            List<SudirGeocodeResultDTO> raw = restClient.get(url, new ParameterizedTypeReference<List<SudirGeocodeResultDTO>>() {}, headers);
            double latencyMs = (System.nanoTime() - start) / 1_000_000.0;
            return GeocoderSearchResult.ok(mapper.fromGeocodeList(raw, latencyMs));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 429) {
                log.warn("Sudir rate limited for '{}', retrying after {}ms", request.full_address(), RETRY_WAIT_MS);
                sleep(RETRY_WAIT_MS);
                try {
                    String url = buildSearchUrl(request);
                    long start = System.nanoTime();
                    List<SudirGeocodeResultDTO> raw = restClient.get(url, new ParameterizedTypeReference<List<SudirGeocodeResultDTO>>() {}, headers);
                    double latencyMs = (System.nanoTime() - start) / 1_000_000.0;
                    return GeocoderSearchResult.ok(mapper.fromGeocodeList(raw, latencyMs));
                } catch (Exception retryEx) {
                    log.warn("Sudir retry failed for '{}': {}", request.full_address(), retryEx.getMessage());
                    return GeocoderSearchResult.error(429);
                }
            }
            log.warn("Sudir HTTP {} for '{}'", e.getStatusCode().value(), request.full_address());
            return GeocoderSearchResult.error(e.getStatusCode().value());
        } catch (Exception e) {
            log.warn("Sudir error for '{}': {}", request.full_address(), e.getMessage());
            return GeocoderSearchResult.error(0);
        }
    }

    private String buildSearchUrl(GeocodeRequestSearch request) {
        return new UrlBuilder()
            .baseUrl(properties.getBaseUrl())
            .path(properties.getEndpoints().getSearch())
            .params(mapper.toSudirGeocodeParamsDTO(request).toMap())
            .build();
    }

    @Override
    public List<GeocodeResponse> reverse(GeocodeRequestReverse request) {
        String url = new UrlBuilder()
            .baseUrl(properties.getBaseUrl())
            .path(properties.getEndpoints().getReverse())
            .params(mapper.toSudirReverseParamsDTO(request).toMap())
            .build();
        long start = System.nanoTime();
        List<SudirReverseResultDTO> raw = restClient.get(url, new ParameterizedTypeReference<List<SudirReverseResultDTO>>() {}, headers);
        double latencyMs = (System.nanoTime() - start) / 1_000_000.0;
        return mapper.fromReverseList(raw, latencyMs);
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}

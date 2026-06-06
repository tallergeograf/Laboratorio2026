package uy.edu.taller.sige.geo_api.utils;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient{

    private final RestTemplate restTemplate;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T get(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    public <T> T get(String url, Class<T> responseType, HttpHeaders headers) {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType).getBody();
    }

    public <T> T get(String url, ParameterizedTypeReference<T> responseType, HttpHeaders headers) {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType).getBody();
    }

    public <T, R> R post(String url, T body, Class<R> responseType) {
        return restTemplate.postForObject(url, body, responseType);
    }

    public <T, R> R post(String url, T body, Class<R> responseType, HttpHeaders headers) {
        HttpEntity<T> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType).getBody();
    }
}
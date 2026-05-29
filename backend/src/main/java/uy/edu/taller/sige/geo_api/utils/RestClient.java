package uy.edu.taller.sige.geo_api.utils;

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

    public <T, R> R post(String url, T body, Class<R> responseType) {
        return restTemplate.postForObject(url, body, responseType);
    }
}
package uy.edu.taller.sige.geo_api.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlBuilder {
    private String baseUrl;
    private String path;
    private Map<String, String> queryParams = new LinkedHashMap<>();
    private Map<String, List<String>> multiParams = new LinkedHashMap<>();
    private Map<String, String> rawParams = new LinkedHashMap<>();

    public UrlBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public UrlBuilder path(String path) {
        this.path = path.startsWith("/") ? path : "/" + path;
        return this;
    }

    public UrlBuilder params(Map<String, String> params) {
        this.queryParams.putAll(params);
        return this;
    }

    public UrlBuilder addParam(String key, String value) {
        this.queryParams.put(key, value);
        return this;
    }

    public UrlBuilder multiParams(Map<String, List<String>> params) {
        this.multiParams.putAll(params);
        return this;
    }

    public UrlBuilder rawParams(Map<String, String> params) {
        this.rawParams.putAll(params);
        return this;
    }

    public String build() {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("La URL base es obligatoria");
        }

        StringBuilder url = new StringBuilder();
        url.append(baseUrl);

        if (path != null && !path.isEmpty()) {
            url.append(path);
        }

        boolean hasQuery = false;
        if (!queryParams.isEmpty()) {
            String query = queryParams.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
                .collect(Collectors.joining("&"));
            if (!query.isBlank()) {
                url.append("?").append(query);
                hasQuery = true;
            }
        }

        for (Map.Entry<String, String> entry : rawParams.entrySet()) {
            if (entry.getValue() == null) continue;
            url.append(hasQuery ? "&" : "?");
            url.append(encode(entry.getKey())).append("=").append(entry.getValue());
            hasQuery = true;
        }

        for (Map.Entry<String, List<String>> entry : multiParams.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) continue;
            for (String value : entry.getValue()) {
                if (value == null) continue;
                url.append(hasQuery ? "&" : "?");
                url.append(encode(entry.getKey())).append("=").append(encode(value));
                hasQuery = true;
            }
        }

        return url.toString();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

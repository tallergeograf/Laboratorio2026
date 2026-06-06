package uy.edu.taller.sige.geo_api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "geocoder.sudir")
public class SudirProperties {
    private String baseUrl;
    private String userAgent;
    private Endpoints endpoints;

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public Endpoints getEndpoints() { return endpoints; }
    public void setEndpoints(Endpoints endpoints) { this.endpoints = endpoints; }

    public static class Endpoints {
        private String search;
        private String reverse;

        public String getSearch() { return search; }
        public void setSearch(String search) { this.search = search; }

        public String getReverse() { return reverse; }
        public void setReverse(String reverse) { this.reverse = reverse; }
    }
}

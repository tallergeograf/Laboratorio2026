package uy.edu.taller.sige.geo_api.dto.nominatim.request;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NominatimSearchParamsDTO(
    @JsonProperty("q")               String q,
    @JsonProperty("street")          String street,
    @JsonProperty("city")            String city,
    @JsonProperty("county")          String county,
    @JsonProperty("state")           String state,
    @JsonProperty("country")         String country,
    @JsonProperty("postalcode")      String postalcode,
    @JsonProperty("format")          String format,          // "json", "jsonv2", "geojson", "geocodejson"
    @JsonProperty("limit")           Integer limit,          // 1 - 40
    @JsonProperty("countrycodes")    String countrycodes,
    @JsonProperty("addressdetails")  Integer addressdetails, // solo 1
    @JsonProperty("extratags")       Integer extratags,      // solo 1
    @JsonProperty("namedetails")     Integer namedetails,    // solo 1
    @JsonProperty("viewbox")         String viewbox,
    @JsonProperty("bounded")         Integer bounded,        // solo 1
    @JsonProperty("accept-language") String acceptLanguage
) {
    public Map<String, String> toMap() {
        Map<String, String> params = new LinkedHashMap<>();
        if (q != null) params.put("q", q);
        if (street != null) params.put("street", street);
        if (city != null) params.put("city", city);
        if (county != null) params.put("county", county);
        if (state != null) params.put("state", state);
        if (country != null) params.put("country", country);
        if (postalcode != null) params.put("postalcode", postalcode);
        if (format != null) params.put("format", format);
        if (limit != null) params.put("limit", limit.toString());
        if (countrycodes != null) params.put("countrycodes", countrycodes);
        if (addressdetails != null) params.put("addressdetails", addressdetails.toString());
        if (extratags != null) params.put("extratags", extratags.toString());
        if (namedetails != null) params.put("namedetails", namedetails.toString());
        if (viewbox != null) params.put("viewbox", viewbox);
        if (bounded != null) params.put("bounded", bounded.toString());
        if (acceptLanguage != null) params.put("accept-language", acceptLanguage);
        return params;
    }
}

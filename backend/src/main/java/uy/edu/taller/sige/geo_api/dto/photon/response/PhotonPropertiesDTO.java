package uy.edu.taller.sige.geo_api.dto.photon.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public record PhotonPropertiesDTO(
    @JsonProperty("osm_id")      Long osmId,
    @JsonProperty("osm_type")    String osmType,       // "N", "W", "R"
    @JsonProperty("osm_key")     String osmKey,
    @JsonProperty("osm_value")   String osmValue,
    @JsonProperty("name")        String name,
    @JsonProperty("housenumber") String housenumber,
    @JsonProperty("street")      String street,
    @JsonProperty("district")    String district,
    @JsonProperty("postcode")    String postcode,
    @JsonProperty("city")        String city,
    @JsonProperty("county")      String county,
    @JsonProperty("state")       String state,
    @JsonProperty("country")     String country,
    @JsonProperty("countrycode") String countrycode,
    @JsonProperty("type")        String type,
    @JsonProperty("extent")      List<Double> extent,  // [n, n, n, n]
    @JsonProperty("extra")       Map<String, Object> extra
) {}
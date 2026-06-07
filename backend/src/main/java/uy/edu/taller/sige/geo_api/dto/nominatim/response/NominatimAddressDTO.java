package uy.edu.taller.sige.geo_api.dto.nominatim.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NominatimAddressDTO(
    @JsonProperty("house_number")    String houseNumber,
    @JsonProperty("road")            String road,
    @JsonProperty("suburb")          String suburb,
    @JsonProperty("neighbourhood")   String neighbourhood,
    @JsonProperty("quarter")         String quarter,
    @JsonProperty("city")            String city,
    @JsonProperty("town")            String town,
    @JsonProperty("village")         String village,
    @JsonProperty("municipality")    String municipality,
    @JsonProperty("county")          String county,
    @JsonProperty("state")           String state,
    @JsonProperty("ISO3166-2-lvl4")  String iso31662Lvl4,
    @JsonProperty("postcode")        String postcode,
    @JsonProperty("country")         String country,
    @JsonProperty("country_code")    String countryCode
) {}
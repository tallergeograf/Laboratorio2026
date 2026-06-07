package uy.edu.taller.sige.geo_api.dto.nominatim.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record NominatimPlaceDTO(
    @JsonProperty("place_id")     Long placeId,
    @JsonProperty("licence")      String licence,
    @JsonProperty("osm_type")     String osmType,       // "node", "way", "relation"
    @JsonProperty("osm_id")       Long osmId,
    @JsonProperty("lat")          String lat,
    @JsonProperty("lon")          String lon,
    @JsonProperty("place_rank")   Integer placeRank,
    @JsonProperty("category")     String category,
    @JsonProperty("type")         String type,
    @JsonProperty("importance")   Double importance,
    @JsonProperty("addresstype")  String addresstype,
    @JsonProperty("name")         String name,
    @JsonProperty("display_name") String displayName,
    @JsonProperty("address")      NominatimAddressDTO address,
    @JsonProperty("boundingbox")  List<String> boundingbox  // [s, n, w, e]
) {}
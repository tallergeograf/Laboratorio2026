package uy.edu.taller.sige.geo_api.dto.photon.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record PhotonReverseParamsDTO(
    @JsonProperty("lat")      Double lat,                     // obligatorio
    @JsonProperty("lon")      Double lon,                     // obligatorio
    @JsonProperty("radius")   Double radius,                  // 0 - 5000
    @JsonProperty("limit")    Integer limit,
    @JsonProperty("osm_tag")  List<String> osmTag,
    @JsonProperty("lang")     String lang
) {
    public Map<String, String> toMap(){
        Map<String, String> params = new LinkedHashMap<>();
        if (lat != null) params.put("lat", lat.toString());
        if (lon != null) params.put("lon", lon.toString());
        if (radius != null) params.put("radius", radius.toString());
        if (limit != null) params.put("limit", limit.toString());
        if (osmTag != null && !osmTag.isEmpty()) params.put("osm_tag", String.join(",", osmTag));
        if (lang != null) params.put("lang", lang);
        return params;
    }

}

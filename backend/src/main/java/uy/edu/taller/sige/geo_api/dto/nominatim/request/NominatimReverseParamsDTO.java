package uy.edu.taller.sige.geo_api.dto.nominatim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.Map;

public record NominatimReverseParamsDTO(
    @JsonProperty("lat")            Double lat,             // obligatorio
    @JsonProperty("lon")            Double lon,             // obligatorio
    @JsonProperty("format")         String format,          // "json", "jsonv2", "geojson", "geocodejson"
    @JsonProperty("zoom")           Integer zoom,           // 0 - 18
    @JsonProperty("addressdetails") Integer addressdetails, // solo 1
    @JsonProperty("layer")          String layer
) {
    public Map<String, String> toMap() {
        Map<String, String> params = new LinkedHashMap<>();
        if (lat != null) params.put("lat", lat.toString());
        if (lon != null) params.put("lon", lon.toString());
        if (format != null) params.put("format", format);
        if (zoom != null) params.put("zoom", zoom.toString());
        if (addressdetails != null) params.put("addressdetails", addressdetails.toString());
        if (layer != null) params.put("layer", layer);
        return params;
    }
}

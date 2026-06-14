package uy.edu.taller.sige.geo_api.dto.photon.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record PhotonSearchParamsDTO(
    @JsonProperty("q")                    String q,
    @JsonProperty("lat")                  Double lat,
    @JsonProperty("lon")                  Double lon,
    @JsonProperty("limit")                Integer limit,
    @JsonProperty("lang")                 String lang,        // "default", "de", "en", "fr"
    @JsonProperty("bbox")                 String bbox,
    @JsonProperty("location_bias_scale")  Double locationBiasScale,
    @JsonProperty("osm_tag")              List<String> osmTag,
    @JsonProperty("layer")                List<String> layer,
    @JsonProperty("dedupe")               Integer dedupe,     // solo acepta 0
    @JsonProperty("debug")                Integer debug       // solo acepta 1
) {

    public Map<String, String> toMap(){
        Map<String, String> params = new LinkedHashMap<>();
        if (q != null) params.put("q", q);
        if (lat != null) params.put("lat", lat.toString());
        if (lon != null) params.put("lon", lon.toString());
        if (limit != null) params.put("limit", limit.toString());
        if (lang != null) params.put("lang", lang);
        if (locationBiasScale != null) params.put("location_bias_scale", locationBiasScale.toString());
        if (dedupe != null) params.put("dedupe", dedupe.toString());
        if (debug != null) params.put("debug", debug.toString());
        return params;
    }

    public Map<String, String> toRawMap() {
        Map<String, String> raw = new LinkedHashMap<>();
        if (bbox != null && !bbox.isBlank()) raw.put("bbox", bbox);
        return raw;
    }

    public Map<String, List<String>> toMultiMap() {
        Map<String, List<String>> multi = new LinkedHashMap<>();
        if (osmTag != null && !osmTag.isEmpty()) multi.put("osm_tag", osmTag);
        if (layer != null && !layer.isEmpty()) multi.put("layer", layer);
        return multi;
    }

}

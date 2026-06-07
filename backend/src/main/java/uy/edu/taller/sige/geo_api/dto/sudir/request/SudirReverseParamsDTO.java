package uy.edu.taller.sige.geo_api.dto.sudir.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.Map;

public record SudirReverseParamsDTO(
    @JsonProperty("latitud")  Double latitud,  // obligatorio
    @JsonProperty("longitud") Double longitud, // obligatorio
    @JsonProperty("limit")    Integer limit
) {
    public Map<String, String> toMap() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("latitud", latitud.toString());
        params.put("longitud", longitud.toString());
        if (limit != null) {
            params.put("limit", limit.toString());
        }
        return params;
    }

}

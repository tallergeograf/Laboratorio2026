package uy.edu.taller.sige.geo_api.dto.sudir.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.Map;

public record SudirGeocodeParamsDTO(
    @JsonProperty("calle")        String calle,        // obligatorio
    @JsonProperty("departamento") String departamento, // obligatorio
    @JsonProperty("localidad")    String localidad     // obligatorio
) {
    public Map<String, String> toMap() {
        Map<String, String> params = new LinkedHashMap<>();
        if (calle != null)  params.put("calle", calle);
        if (departamento != null) params.put("departamento", departamento);
        if (localidad != null) params.put("localidad", localidad);
        return params;
    }

}

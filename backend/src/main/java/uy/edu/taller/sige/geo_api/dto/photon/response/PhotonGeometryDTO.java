package uy.edu.taller.sige.geo_api.dto.photon.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PhotonGeometryDTO(
    @JsonProperty("type")        String type,          // "Point", "Polygon", "LineString", "MultiPolygon"
    @JsonProperty("coordinates") List<Object> coordinates
) {}

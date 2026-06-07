package uy.edu.taller.sige.geo_api.dto.photon.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhotonFeatureDTO(
    @JsonProperty("type")       String type,           // siempre "Feature"
    @JsonProperty("geometry")   PhotonGeometryDTO geometry,
    @JsonProperty("properties") PhotonPropertiesDTO properties
) {}
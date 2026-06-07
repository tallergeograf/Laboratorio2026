package uy.edu.taller.sige.geo_api.dto.photon.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PhotonResponseDTO(
    @JsonProperty("type")     String type,             // siempre "FeatureCollection"
    @JsonProperty("features") List<PhotonFeatureDTO> features
) {}
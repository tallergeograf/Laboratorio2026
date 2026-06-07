package uy.edu.taller.sige.geo_api.dto.sudir.response;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SudirCalleDTO(
    @JsonProperty("idCalle")           Integer idCalle,
    @JsonProperty("nombre_normalizado") String nombreNormalizado
) {}

package uy.edu.taller.sige.geo_api.dto.sudir.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SudirLocalidadDTO(
    @JsonProperty("idLocalidad")       Integer idLocalidad,
    @JsonProperty("nombre_normalizado") String nombreNormalizado
) {}
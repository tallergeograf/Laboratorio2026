package uy.edu.taller.sige.geo_api.dto.sudir.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SudirInmuebleDTO(
    @JsonProperty("nombre")         String nombre,
    @JsonProperty("idPuntoNotable") Integer idPuntoNotable
) {}
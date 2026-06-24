package uy.edu.taller.sige.geo_api.dto.sudir.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SudirNumeroDTO(
    @JsonProperty("nro_puerta") Integer nroPuerta
) {}
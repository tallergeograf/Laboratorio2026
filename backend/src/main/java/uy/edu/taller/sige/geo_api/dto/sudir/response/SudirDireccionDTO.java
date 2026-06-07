package uy.edu.taller.sige.geo_api.dto.sudir.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SudirDireccionDTO(
    @JsonProperty("departamento") SudirDepartamentoDTO departamento,
    @JsonProperty("localidad")    SudirLocalidadDTO localidad,
    @JsonProperty("calle")        SudirCalleDTO calle,       // opcional
    @JsonProperty("numero")       SudirNumeroDTO numero,     // opcional
    @JsonProperty("inmueble")     SudirInmuebleDTO inmueble  // opcional
) {}
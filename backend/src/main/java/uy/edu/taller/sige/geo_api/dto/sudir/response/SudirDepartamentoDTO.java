package uy.edu.taller.sige.geo_api.dto.sudir.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SudirDepartamentoDTO(
    @JsonProperty("idDepartamento")    Integer idDepartamento,
    @JsonProperty("nombre_normalizado") String nombreNormalizado
) {}
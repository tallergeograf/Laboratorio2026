package uy.edu.taller.sige.geo_api.dto.sudir.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SudirGeocodeResultDTO(
    @JsonProperty("direccion")              SudirDireccionDTO direccion,
    @JsonProperty("codigoPostal")           Integer codigoPostal,
    @JsonProperty("codigoPostalAmpliado")   Integer codigoPostalAmpliado,
    @JsonProperty("puntoX")                 Double puntoX,
    @JsonProperty("puntoY")                 Double puntoY,
    @JsonProperty("idPunto")                Integer idPunto,
    @JsonProperty("srid")                   Integer srid,
    @JsonProperty("idTipoClasificacion")    Integer idTipoClasificacion,
    @JsonProperty("error")                  String error
) {}

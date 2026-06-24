package uy.edu.taller.sige.geo_api.dto.sudir.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SudirReverseResultDTO(
    @JsonProperty("type")           String type,
    @JsonProperty("id")             String id,
    @JsonProperty("address")        String address,
    @JsonProperty("idCalle")        Integer idCalle,
    @JsonProperty("nomVia")         String nomVia,
    @JsonProperty("postalCode")     String postalCode,
    @JsonProperty("idLocalidad")    Integer idLocalidad,
    @JsonProperty("localidad")      String localidad,
    @JsonProperty("idDepartamento") Integer idDepartamento,
    @JsonProperty("departamento")   String departamento,
    @JsonProperty("manzana")        String manzana,         // nullable
    @JsonProperty("solar")          String solar,           // nullable
    @JsonProperty("inmueble")       String inmueble,        // nullable
    @JsonProperty("idCalleEsq")     Integer idCalleEsq,
    @JsonProperty("km")             Double km,
    @JsonProperty("priority")       Double priority,
    @JsonProperty("geom")           Object geom,            // unknown, nullable
    @JsonProperty("tip_via")        String tipVia,          // nullable
    @JsonProperty("lat")            Double lat,
    @JsonProperty("lng")            Double lng,
    @JsonProperty("portalNumber")   Integer portalNumber,
    @JsonProperty("letra")          String letra,           // nullable
    @JsonProperty("stateMsg")       String stateMsg,
    @JsonProperty("source")         String source,
    @JsonProperty("ranking")        Double ranking,
    @JsonProperty("state")          Integer state
) {}
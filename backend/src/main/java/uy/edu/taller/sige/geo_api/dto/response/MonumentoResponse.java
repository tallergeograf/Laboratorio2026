package uy.edu.taller.sige.geo_api.dto.response;

public record MonumentoResponse(Long id, Long gid, String nombre, Double latitud, Double longitud, String direccion, String localidad) {}

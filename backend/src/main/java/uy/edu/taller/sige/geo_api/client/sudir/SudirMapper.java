package uy.edu.taller.sige.geo_api.client.sudir;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;
import uy.edu.taller.sige.geo_api.model.enums.GeoScope;
import uy.edu.taller.sige.geo_api.dto.sudir.request.SudirGeocodeParamsDTO;
import uy.edu.taller.sige.geo_api.dto.sudir.request.SudirReverseParamsDTO;
import uy.edu.taller.sige.geo_api.dto.sudir.response.SudirDireccionDTO;
import uy.edu.taller.sige.geo_api.dto.sudir.response.SudirGeocodeResultDTO;
import uy.edu.taller.sige.geo_api.dto.sudir.response.SudirReverseResultDTO;

@Component
public class SudirMapper {

    public SudirGeocodeParamsDTO toSudirGeocodeParamsDTO(GeocodeRequestSearch request) {
        String department = request.geoScope() == GeoScope.MONTEVIDEO ? "Montevideo" : request.department();
        return new SudirGeocodeParamsDTO(request.full_address(), department, null);
    }

    public SudirReverseParamsDTO toSudirReverseParamsDTO(GeocodeRequestReverse request) {
        return new SudirReverseParamsDTO(request.lat(), request.lon(), null);
    }

    public GeocodeResponse fromGeocode(SudirGeocodeResultDTO result, double latencyMs) {
        SudirDireccionDTO dir = result.direccion();
        String inmuebleName = dir != null && dir.inmueble() != null ? dir.inmueble().nombre() : null;
        String street      = dir != null && dir.calle() != null ? dir.calle().nombreNormalizado() : null;
        String houseNumber = dir != null && dir.numero() != null && dir.numero().nroPuerta() != null
            ? dir.numero().nroPuerta().toString() : null;
        String city        = dir != null && dir.localidad() != null ? dir.localidad().nombreNormalizado() : null;
        String state       = dir != null && dir.departamento() != null ? dir.departamento().nombreNormalizado() : null;
        String postcode    = result.codigoPostal() != null ? result.codigoPostal().toString() : null;

        return new GeocodeResponse(
            buildDisplayName(inmuebleName, street, houseNumber, city, state),
            result.puntoY(),
            result.puntoX(),
            street,
            houseNumber,
            city,
            state,
            "Uruguay",
            "UY",
            postcode,
            "sudir",
            null,
            null,
            result.error() != null && !result.error().isBlank(),
            latencyMs
        );
    }

    public GeocodeResponse fromReverse(SudirReverseResultDTO result, double latencyMs) {
        String houseNumber = result.portalNumber() != null ? result.portalNumber().toString() : null;
        return new GeocodeResponse(
            result.address(),
            result.lat(),
            result.lng(),
            result.nomVia(),
            houseNumber,
            result.localidad(),
            result.departamento(),
            "Uruguay",
            "UY",
            result.postalCode(),
            "sudir",
            null,
            null,
            false,
            latencyMs
        );
    }

    public List<GeocodeResponse> fromGeocodeList(List<SudirGeocodeResultDTO> results, double latencyMs) {
        return results.stream()
            .filter(r -> r.direccion() != null && r.direccion().calle() != null)
            .map(r -> fromGeocode(r, latencyMs))
            .toList();
    }

    public List<GeocodeResponse> fromReverseList(List<SudirReverseResultDTO> results, double latencyMs) {
        return results.stream().map(r -> fromReverse(r, latencyMs)).toList();
    }

    private String buildDisplayName(String inmuebleName, String street, String houseNumber, String city, String state) {
        String streetWithNumber = street != null && houseNumber != null ? street + " " + houseNumber : street;
        return Stream.of(inmuebleName, streetWithNumber, city, state, "Uruguay")
            .filter(s -> s != null && !s.isBlank())
            .collect(Collectors.joining(", "));
    }
}

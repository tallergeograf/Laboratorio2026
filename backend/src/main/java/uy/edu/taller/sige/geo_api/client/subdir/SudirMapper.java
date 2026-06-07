package uy.edu.taller.sige.geo_api.client.subdir;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;
import uy.edu.taller.sige.geo_api.dto.sudir.request.SudirGeocodeParamsDTO;
import uy.edu.taller.sige.geo_api.dto.sudir.request.SudirReverseParamsDTO;
import uy.edu.taller.sige.geo_api.dto.sudir.response.SudirDireccionDTO;
import uy.edu.taller.sige.geo_api.dto.sudir.response.SudirGeocodeResultDTO;
import uy.edu.taller.sige.geo_api.dto.sudir.response.SudirReverseResultDTO;

@Component
public class SudirMapper {

    public SudirGeocodeParamsDTO toSudirGeocodeParamsDTO(GeocodeRequestSearch request) {
        return new SudirGeocodeParamsDTO(request.street(), request.state(), request.city());
    }

    public SudirReverseParamsDTO toSudirReverseParamsDTO(GeocodeRequestReverse request) {
        return new SudirReverseParamsDTO(request.lat(), request.lon(), null);
    }

    public GeocodeResponse fromGeocode(SudirGeocodeResultDTO result) {
        SudirDireccionDTO dir = result.direccion();
        String street      = dir != null && dir.calle() != null ? dir.calle().nombreNormalizado() : null;
        String houseNumber = dir != null && dir.numero() != null && dir.numero().nroPuerta() != null
            ? dir.numero().nroPuerta().toString() : null;
        String city        = dir != null && dir.localidad() != null ? dir.localidad().nombreNormalizado() : null;
        String state       = dir != null && dir.departamento() != null ? dir.departamento().nombreNormalizado() : null;
        String postcode    = result.codigoPostal() != null ? result.codigoPostal().toString() : null;

        return new GeocodeResponse(
            buildDisplayName(street, houseNumber, city, state),
            result.puntoY(),
            result.puntoX(),
            street,
            houseNumber,
            city,
            state,
            "Uruguay",
            "UY",
            postcode,
            "sudir"
        );
    }

    public GeocodeResponse fromReverse(SudirReverseResultDTO result) {
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
            "sudir"
        );
    }

    public List<GeocodeResponse> fromGeocodeList(List<SudirGeocodeResultDTO> results) {
        return results.stream().map(this::fromGeocode).toList();
    }

    public List<GeocodeResponse> fromReverseList(List<SudirReverseResultDTO> results) {
        return results.stream().map(this::fromReverse).toList();
    }

    private String buildDisplayName(String street, String houseNumber, String city, String state) {
        String streetWithNumber = street != null && houseNumber != null ? street + " " + houseNumber : street;
        return Stream.of(streetWithNumber, city, state, "Uruguay")
            .filter(s -> s != null && !s.isBlank())
            .collect(Collectors.joining(", "));
    }
}

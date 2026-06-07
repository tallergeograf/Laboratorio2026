package uy.edu.taller.sige.geo_api.client.nominatim;

import java.util.List;

import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.dto.nominatim.request.NominatimReverseParamsDTO;
import uy.edu.taller.sige.geo_api.dto.nominatim.request.NominatimSearchParamsDTO;
import uy.edu.taller.sige.geo_api.dto.nominatim.response.NominatimAddressDTO;
import uy.edu.taller.sige.geo_api.dto.nominatim.response.NominatimPlaceDTO;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;

@Component
public class NominatimMapper {

    public NominatimSearchParamsDTO toNominatimSearchParamsDTO(GeocodeRequestSearch request) {
        return new NominatimSearchParamsDTO(
            request.query(), null, null, null, null, null, null,
            "jsonv2", request.limit(), null, 1, null, null, null, null, null
        );
    }

    public NominatimReverseParamsDTO toNominatimReverseParamsDTO(GeocodeRequestReverse request) {
        return new NominatimReverseParamsDTO(request.lat(), request.lon(), "jsonv2", null, 1, null);
    }

    public GeocodeResponse toGeocodeResponse(NominatimPlaceDTO place) {
        NominatimAddressDTO addr = place.address();
        String city = addr != null
            ? (addr.city() != null ? addr.city()
               : addr.town() != null ? addr.town()
               : addr.village())
            : null;
        return new GeocodeResponse(
            place.displayName(),
            place.lat() != null ? Double.parseDouble(place.lat()) : null,
            place.lon() != null ? Double.parseDouble(place.lon()) : null,
            addr != null ? addr.road() : null,
            addr != null ? addr.houseNumber() : null,
            city,
            addr != null ? addr.state() : null,
            addr != null ? addr.country() : null,
            addr != null ? addr.countryCode() : null,
            addr != null ? addr.postcode() : null,
            "nominatim"
        );
    }

    public List<GeocodeResponse> toGeocodeResponseList(List<NominatimPlaceDTO> places) {
        return places.stream().map(this::toGeocodeResponse).toList();
    }
}

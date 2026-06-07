package uy.edu.taller.sige.geo_api.client.photon;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.dto.photon.request.PhotonReverseParamsDTO;
import uy.edu.taller.sige.geo_api.dto.photon.request.PhotonSearchParamsDTO;
import uy.edu.taller.sige.geo_api.dto.photon.response.PhotonFeatureDTO;
import uy.edu.taller.sige.geo_api.dto.photon.response.PhotonPropertiesDTO;
import uy.edu.taller.sige.geo_api.dto.photon.response.PhotonResponseDTO;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;

@Component
public class PhotonMapper {
    
    public PhotonSearchParamsDTO toPhotonSearchParamsDTO(GeocodeRequestSearch request) {
        return new PhotonSearchParamsDTO(
            request.query(), null, null, request.limit(), null, null, null, null, null, null, null
        );
    }

    public PhotonReverseParamsDTO toPhotonReverseParamsDTO(GeocodeRequestReverse request) {
        return new PhotonReverseParamsDTO(request.lat(), request.lon(), null, null, null, null);
    }

    public GeocodeResponse toGeocodeResponse(PhotonFeatureDTO feature) {
        PhotonPropertiesDTO props = feature.properties();
        // GeoJSON coordinates order: [lon, lat]
        List<Object> coords = feature.geometry() != null ? feature.geometry().coordinates() : null;
        Double lon = coords != null && coords.size() > 0 ? toDouble(coords.get(0)) : null;
        Double lat = coords != null && coords.size() > 1 ? toDouble(coords.get(1)) : null;

        return new GeocodeResponse(
            buildDisplayName(props),
            lat,
            lon,
            props != null ? props.street() : null,
            props != null ? props.housenumber() : null,
            props != null ? props.city() : null,
            props != null ? props.state() : null,
            props != null ? props.country() : null,
            props != null ? props.countrycode() : null,
            props != null ? props.postcode() : null,
            "photon"
        );
    }

    public List<GeocodeResponse> toGeocodeResponseList(PhotonResponseDTO response) {
        if (response == null || response.features() == null) return List.of();
        return response.features().stream().map(this::toGeocodeResponse).toList();
    }

    private String buildDisplayName(PhotonPropertiesDTO props) {
        if (props == null) return null;
        return Stream.of(props.name(), props.street(), props.city(), props.state(), props.country())
            .filter(s -> s != null && !s.isBlank())
            .collect(Collectors.joining(", "));
    }

    private Double toDouble(Object value) {
        if (value instanceof Number n) return n.doubleValue();
        try { return Double.parseDouble(value.toString()); } catch (NumberFormatException e) { return null; }
    }
}

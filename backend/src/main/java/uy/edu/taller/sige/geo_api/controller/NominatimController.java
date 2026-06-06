package uy.edu.taller.sige.geo_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;
import uy.edu.taller.sige.geo_api.enums.GeocoderProvider;
import uy.edu.taller.sige.geo_api.service.geocoding.GeocodingService;

@RestController
@RequestMapping("/api/nominatim")
public class NominatimController {

    private final GeocodingService geocodingService;

    public NominatimController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping("/search")
    public List<GeocodeResponse> search(GeocodeRequestSearch request) {
        return geocodingService.search(GeocoderProvider.NOMINATIM, request);
    }

    @GetMapping("/reverse")
    public List<GeocodeResponse> reverse(GeocodeRequestReverse request) {
        return geocodingService.reverse(GeocoderProvider.NOMINATIM, request);
    }
}

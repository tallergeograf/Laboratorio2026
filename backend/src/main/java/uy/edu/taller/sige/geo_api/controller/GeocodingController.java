package uy.edu.taller.sige.geo_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uy.edu.taller.sige.geo_api.enums.GeocoderProvider;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestReverse;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;
import uy.edu.taller.sige.geo_api.service.geocoding.GeocodingService;

@RestController
@RequestMapping("/api/geocoding")
public class GeocodingController {

    private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @PostMapping("/{provider}/search")
    public List<GeocodeResponse> search(
            @PathVariable GeocoderProvider provider,
            @RequestBody GeocodeRequestSearch request) {
        return geocodingService.search(provider, request);
    }

    @PostMapping("/{provider}/reverse")
    public List<GeocodeResponse> reverse(
            @PathVariable GeocoderProvider provider,
            @RequestBody GeocodeRequestReverse request) {
        return geocodingService.reverse(provider, request);
    }
}

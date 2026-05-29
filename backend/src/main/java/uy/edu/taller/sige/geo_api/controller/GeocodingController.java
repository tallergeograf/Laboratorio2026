package uy.edu.taller.sige.geo_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uy.edu.taller.sige.geo_api.service.geocoding.GeocodingService;

@RestController
@RequestMapping("/api/geocoding")
public class GeocodingController {

    private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    /*@PostMapping("/forward")
    public GeocodeResponse geocode(@RequestBody GeocodeRequest request) {
        return geocodingService.geocode(request);
    }*/
}
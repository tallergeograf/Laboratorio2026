package uy.edu.taller.sige.geo_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uy.edu.taller.sige.geo_api.service.gis.GisService;
@RestController
@RequestMapping("/api/gis")
public class GisDataController {

    private final GisService gisService;

    public GisDataController(GisService gisService) {
        this.gisService = gisService;
    }

    /*@GetMapping("/locations")
    public List<GisResponse> getLocations() {
        return gisService.getAllLocations();
    }*/
}
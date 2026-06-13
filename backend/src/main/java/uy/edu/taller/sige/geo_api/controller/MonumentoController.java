package uy.edu.taller.sige.geo_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.edu.taller.sige.geo_api.dto.response.MonumentoResponse;
import uy.edu.taller.sige.geo_api.dto.response.NearestMonumentoResponse;
import uy.edu.taller.sige.geo_api.model.enums.GeocoderProvider;
import uy.edu.taller.sige.geo_api.service.monumentos.MonumentoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monumentos")
public class MonumentoController {

    private final MonumentoService monumentoService;

    public MonumentoController(MonumentoService monumentoService) {
        this.monumentoService = monumentoService;
    }

    @PostMapping("/import")
    public ResponseEntity<Map<String, Integer>> importMonumentos() {
        int count = monumentoService.importData();
        return ResponseEntity.ok(Map.of("imported", count));
    }

    @GetMapping
    public List<MonumentoResponse> getAll() {
        return monumentoService.findAll();
    }

    @GetMapping("/nearest")
    public ResponseEntity<List<NearestMonumentoResponse>> getNearestMonumentos(
            @RequestParam String address,
            @RequestParam GeocoderProvider provider,
            @RequestParam(defaultValue = "5") int limit) {
        List<NearestMonumentoResponse> results = monumentoService.findNearest(address, provider, limit);
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/within")
    public ResponseEntity<List<NearestMonumentoResponse>> getMonumentosWithinRadius(
            @RequestParam String address,
            @RequestParam GeocoderProvider provider,
            @RequestParam(defaultValue = "500") double radius) {
        List<NearestMonumentoResponse> results = monumentoService.findWithinRadius(address, provider, radius);
        return ResponseEntity.ok(results);
    }
}

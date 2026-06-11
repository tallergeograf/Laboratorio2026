package uy.edu.taller.sige.geo_api.service.monumentos;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;
import uy.edu.taller.sige.geo_api.dto.response.MonumentoResponse;
import uy.edu.taller.sige.geo_api.dto.response.NearestMonumentoResponse;
import uy.edu.taller.sige.geo_api.model.Monumento;
import uy.edu.taller.sige.geo_api.model.enums.GeocoderProvider;
import uy.edu.taller.sige.geo_api.repository.MonumentoRepository;
import uy.edu.taller.sige.geo_api.service.geocoding.GeocodingService;
import uy.edu.taller.sige.geo_api.utils.HaversineCalculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MonumentoServiceImpl implements MonumentoService {

    private static final String CSV_PATH = "datos_monumentos.csv";

    private final MonumentoRepository monumentoRepository;
    private final GeocodingService geocodingService;

    public MonumentoServiceImpl(MonumentoRepository monumentoRepository, GeocodingService geocodingService) {
        this.monumentoRepository = monumentoRepository;
        this.geocodingService = geocodingService;
    }

    @Override
    public int importData() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(CSV_PATH).getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }

                String[] cols = line.split(",", -1);
                if (cols.length < 5) continue;

                // columns: X(lon), Y(lat), GID, MHN, IDENTIFICA, RESOLUCION, DIRECCION, ...
                Double lon = parseDouble(cols[0]);
                Double lat = parseDouble(cols[1]);
                Long gid = parseLong(cols[2].trim());
                String nombre = cols[4].trim();
                String direccion = cols.length > 6 ? cols[6].trim() : null;

                if (lat == null || lon == null || gid == null) continue;
                if (monumentoRepository.existsByOsmId(gid)) continue;

                Monumento m = new Monumento();
                m.setOsmId(gid);
                m.setName(nombre.isEmpty() ? null : nombre);
                m.setLat(lat);
                m.setLon(lon);
                m.setStreet(direccion == null || direccion.isEmpty() ? null : direccion);
                m.setCity("Montevideo");

                monumentoRepository.save(m);
                count++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV: " + e.getMessage(), e);
        }
        return count;
    }

    @Override
    public List<MonumentoResponse> findAll() {
        return monumentoRepository.findAll().stream()
                .map(m -> new MonumentoResponse(m.getId(), m.getOsmId(), m.getName(), m.getLat(), m.getLon(), m.getStreet(), m.getCity()))
                .toList();
    }

    @Override
    public List<NearestMonumentoResponse> findNearest(String address, GeocoderProvider provider, int limit) {
        int effectiveLimit = Math.min(limit, 20);
        GeocodeRequestSearch request = new GeocodeRequestSearch(null, address, null, null, null);
        List<GeocodeResponse> geoResults = geocodingService.search(provider, request);
        if (geoResults == null || geoResults.isEmpty()) {
            return Collections.emptyList();
        }
        GeocodeResponse ref = geoResults.get(0);
        return monumentoRepository.findAll().stream()
                .filter(m -> m.getLat() != null && m.getLon() != null)
                .map(m -> new NearestMonumentoResponse(
                        m.getId(),
                        m.getOsmId(),
                        m.getName(),
                        m.getLat(),
                        m.getLon(),
                        m.getStreet(),
                        m.getCity(),
                        HaversineCalculator.distanceMeters(ref.lat(), ref.lon(), m.getLat(), m.getLon())))
                .sorted(Comparator.comparingDouble(NearestMonumentoResponse::distanceMeters))
                .limit(effectiveLimit)
                .toList();
    }

    private Double parseDouble(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return null; }
    }

    private Long parseLong(String s) {
        try { return Long.parseLong(s.trim()); } catch (Exception e) { return null; }
    }
}

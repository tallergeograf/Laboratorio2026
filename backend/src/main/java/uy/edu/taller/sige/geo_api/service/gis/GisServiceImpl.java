package uy.edu.taller.sige.geo_api.service.gis;

import org.springframework.stereotype.Service;

@Service
public class GisServiceImpl implements GisService {

    /*private final LocationRepository locationRepository;

    public GisServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<GisResponse> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(loc -> new GisResponse(loc))
                .toList();
    }

    @Override
    public GisResponse getById(Long id) {
        return locationRepository.findById(id)
                .map(GisResponse::new)
                .orElse(null);
    }*/
}
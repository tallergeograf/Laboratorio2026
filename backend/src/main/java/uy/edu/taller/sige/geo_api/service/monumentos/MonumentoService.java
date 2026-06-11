package uy.edu.taller.sige.geo_api.service.monumentos;

import uy.edu.taller.sige.geo_api.dto.response.MonumentoResponse;
import uy.edu.taller.sige.geo_api.dto.response.NearestMonumentoResponse;
import uy.edu.taller.sige.geo_api.model.enums.GeocoderProvider;
import java.util.List;

public interface MonumentoService {
    int importData();
    List<MonumentoResponse> findAll();
    List<NearestMonumentoResponse> findNearest(String address, GeocoderProvider provider, int limit);
}

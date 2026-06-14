package uy.edu.taller.sige.geo_api.dto.request;

import uy.edu.taller.sige.geo_api.model.enums.AddressCategory;
import uy.edu.taller.sige.geo_api.model.enums.AddressType;
import uy.edu.taller.sige.geo_api.model.enums.GeoScope;

public record GeocodeRequestSearch(
    Long id,
    String full_address,
    String department,
    AddressType type,
    AddressCategory category,
    GeoScope geoScope
) {
    public GeocodeRequestSearch {
        if (geoScope == null) geoScope = GeoScope.URUGUAY;
    }

    public GeocodeRequestSearch(Long id, String full_address, String department, AddressType type, AddressCategory category) {
        this(id, full_address, department, type, category, GeoScope.URUGUAY);
    }
}

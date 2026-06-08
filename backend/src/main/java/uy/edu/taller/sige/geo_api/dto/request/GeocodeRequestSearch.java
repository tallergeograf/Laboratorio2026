package uy.edu.taller.sige.geo_api.dto.request;

import uy.edu.taller.sige.geo_api.model.enums.AddressCategory;
import uy.edu.taller.sige.geo_api.model.enums.AddressType;

public record GeocodeRequestSearch(
    Long id,
    String full_address,
    String department,
    AddressType type,
    AddressCategory category
) {}

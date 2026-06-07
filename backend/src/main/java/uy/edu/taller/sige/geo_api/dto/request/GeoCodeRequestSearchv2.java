package uy.edu.taller.sige.geo_api.dto.request;

import uy.edu.taller.sige.geo_api.model.enums.AddressCategory;
import uy.edu.taller.sige.geo_api.model.enums.AddressType;

public record GeoCodeRequestSearchv2 (
    Long id,
    String full_address,
    String departament,
    AddressType type,
    AddressCategory category
){}

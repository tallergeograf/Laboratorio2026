package uy.edu.taller.sige.geo_api.dto.stats;

import java.util.List;

public record StatsFilterRequest(
    List<String> departments,
    List<String> category,
    List<String> variacion
) {}

package uy.edu.taller.sige.geo_api.service.statistics;

import org.springframework.stereotype.Service;

import uy.edu.taller.sige.geo_api.dto.stats.StatsAccuracyResponse;
import uy.edu.taller.sige.geo_api.dto.stats.StatsAddressResponse;
import uy.edu.taller.sige.geo_api.dto.stats.StatsCoverageResponse;
import uy.edu.taller.sige.geo_api.dto.stats.StatsFilterRequest;
import uy.edu.taller.sige.geo_api.dto.stats.StatsLatencyResponse;
import uy.edu.taller.sige.geo_api.dto.stats.StatsRealPointResponse;
import uy.edu.taller.sige.geo_api.dto.stats.StatsReliabilityResponse;
import uy.edu.taller.sige.geo_api.dto.stats.StatsRequest;
import uy.edu.taller.sige.geo_api.dto.stats.StatsResponse;
import uy.edu.taller.sige.geo_api.model.SpecificAddressResult;
import uy.edu.taller.sige.geo_api.model.enums.AddressType;
import uy.edu.taller.sige.geo_api.repository.SpecificAddressResultRepository;
import uy.edu.taller.sige.geo_api.utils.HaversineCalculator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final List<String> URBAN_DEPARTMENTS = List.of("MONTEVIDEO", "CANELONES", "MALDONADO");

    private final SpecificAddressResultRepository repository;

    public StatisticsServiceImpl(SpecificAddressResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<StatsResponse> getAllProviderStats(StatsRequest request) {
        boolean isDemo = request != null && Boolean.TRUE.equals(request.demo());

        List<SpecificAddressResult> allResults = repository.findAll().stream()
                .filter(r -> isDemo == Boolean.TRUE.equals(r.getId().getIsDemo()))
                .toList();

        StatsFilterRequest filters = request != null ? request.filters() : null;

        Map<String, List<SpecificAddressResult>> grouped = allResults.stream()
                .collect(Collectors.groupingBy(r -> r.getId().getGeocoderId()));

        return grouped.entrySet().stream()
                .map(e -> buildResponse(e.getKey(), e.getValue(), filters))
                .toList();
    }

    private List<SpecificAddressResult> applyFilters(List<SpecificAddressResult> results, StatsFilterRequest filters) {
        if (filters == null) return results;

        return results.stream()
                .filter(r -> filters.departments() == null || filters.departments().isEmpty()
                        || filters.departments().contains(r.getDireccion().getDepartamento()))
                .filter(r -> filters.category() == null || filters.category().isEmpty()
                        || filters.category().contains(r.getDireccion().getCategoria().name()))
                .filter(r -> filters.variacion() == null || filters.variacion().isEmpty()
                        || filters.variacion().contains(r.getDireccion().getTipoDireccion().name()))
                .toList();
    }

    private StatsResponse buildResponse(String provider, List<SpecificAddressResult> allResults, StatsFilterRequest filters) {
        List<SpecificAddressResult> filtered = applyFilters(allResults, filters);

        List<StatsAddressResponse> entries = filtered.stream()
                .filter(r ->
                        r.getIsResult() &&
                        r.getLatitud() != null &&
                        r.getLongitud() != null &&
                        r.getDireccion().getLatitud() != null &&
                        r.getDireccion().getLongitud() != null
                )
                .map(r -> new StatsAddressResponse(
                        r.getDireccion().getId(),
                        r.getDireccion().getDireccionCompleta(),
                        r.getDireccion().getLatitud(),
                        r.getDireccion().getLongitud(),
                        r.getLatitud(),
                        r.getLongitud(),
                        HaversineCalculator.distanceMeters(r.getDireccion().getLatitud(), r.getDireccion().getLongitud(), r.getLatitud(), r.getLongitud())
                ))
                .toList();

        List<Double> latencies = filtered.stream()
                .map(SpecificAddressResult::getLatencia)
                .filter(l -> l != null)
                .sorted()
                .toList();

        double avgLatency = latencies.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double maxLatency = latencies.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double medianLatency = latencies.isEmpty() ? 0.0
                : latencies.size() % 2 == 1
                        ? latencies.get(latencies.size() / 2)
                        : (latencies.get(latencies.size() / 2 - 1) + latencies.get(latencies.size() / 2)) / 2.0;

        List<Double> errors = entries.stream()
                .map(StatsAddressResponse::errorMeters)
                .sorted()
                .toList();

        double avg = errors.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double max = errors.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double median = errors.isEmpty() ? 0.0
                : errors.size() % 2 == 1
                        ? errors.get(errors.size() / 2)
                        : (errors.get(errors.size() / 2 - 1) + errors.get(errors.size() / 2)) / 2.0;

        double porcentageSuccess = errors.isEmpty() ? 0.0
                : (double) errors.stream().filter(e -> e < 13).count() / errors.size() * 100.0;

        int coverage = entries.size();

        int totalErrorsTypographic = (int) filtered.stream()
                .filter(r -> !r.getIsResult() && List.of(
                        AddressType.ERR1_S, 
                        AddressType.ERR1_B, 
                        AddressType.ERR2_SS, 
                        AddressType.ERR2_SB, 
                        AddressType.ERR2_BS, 
                        AddressType.ERR2_BB
                ).contains(r.getDireccion().getTipoDireccion()))
                .count();

        int totalErrorsPermutation = (int) filtered.stream()
                .filter(r -> !r.getIsResult() && r.getDireccion().getTipoDireccion().equals(AddressType.PERMUTACION))
                .count();

        int totalErrorsComun = (int) filtered.stream()
                .filter(r -> !r.getIsResult() && r.getDireccion().getTipoDireccion().equals(AddressType.COMUN))
                .count();        

        int totalErrorsAbbreviation = (int) filtered.stream()
                .filter(r -> !r.getIsResult() && r.getDireccion().getTipoDireccion().equals(AddressType.ABREVIACION))
                .count();        

        int totalErrorsRural = (int) allResults.stream()
                .filter(r -> !r.getIsResult() && !URBAN_DEPARTMENTS.contains(r.getDireccion().getDepartamento()))
                .count();

        int totalErrorsUrban = (int) allResults.stream()
                .filter(r -> !r.getIsResult() && URBAN_DEPARTMENTS.contains(r.getDireccion().getDepartamento()))
                .count();

        int totalTypographic = (int) filtered.stream()
        .filter(r -> List.of(AddressType.ERR1_S, AddressType.ERR1_B, AddressType.ERR2_SS, AddressType.ERR2_SB, AddressType.ERR2_BS, AddressType.ERR2_BB)
        .contains(r.getDireccion().getTipoDireccion()))
        .count();

        int totalPermutation = (int) filtered.stream()
        .filter(r -> r.getDireccion().getTipoDireccion().equals(AddressType.PERMUTACION))
        .count();

        int totalAbbreviation = (int) filtered.stream()
        .filter(r -> r.getDireccion().getTipoDireccion().equals(AddressType.ABREVIACION))
        .count();

        int totalComun = (int) filtered.stream()
        .filter(r -> r.getDireccion().getTipoDireccion().equals(AddressType.COMUN))
        .count();

        int totalRural = (int) allResults.stream()
        .filter(r -> !URBAN_DEPARTMENTS.contains(r.getDireccion().getDepartamento()))
        .count();

        int totalUrban = (int) allResults.stream()
        .filter(r -> URBAN_DEPARTMENTS.contains(r.getDireccion().getDepartamento()))
        .count();        

        List<StatsRealPointResponse> realPoints = filtered.stream()
                .filter(r -> r.getDireccion().getLatitud() != null && r.getDireccion().getLongitud() != null)
                .map(r -> new StatsRealPointResponse(r.getDireccion().getLatitud(), r.getDireccion().getLongitud()))
                .toList();

        return new StatsResponse(
                provider,
                filtered.size(),
                new StatsAccuracyResponse(avg, max, median, porcentageSuccess),
                new StatsCoverageResponse(coverage),
                new StatsReliabilityResponse(totalErrorsTypographic, totalTypographic, totalErrorsPermutation, totalPermutation, totalErrorsAbbreviation, totalAbbreviation, totalErrorsComun, totalComun, totalErrorsRural, totalRural, totalErrorsUrban, totalUrban),
                new StatsLatencyResponse(avgLatency, medianLatency, maxLatency),
                entries,
                realPoints
        );
    }
}

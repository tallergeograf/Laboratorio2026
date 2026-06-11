package uy.edu.taller.sige.geo_api.service.statistics;

import org.springframework.stereotype.Service;

import uy.edu.taller.sige.geo_api.dto.stats.StatsAccuracyResponse;
import uy.edu.taller.sige.geo_api.dto.stats.StatsAddressResponse;
import uy.edu.taller.sige.geo_api.dto.stats.StatsCoverageResponse;
import uy.edu.taller.sige.geo_api.dto.stats.StatsReliabilityResponse;
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

    private final SpecificAddressResultRepository repository;

    public StatisticsServiceImpl(SpecificAddressResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<StatsResponse> getAllProviderStats() {
        List<SpecificAddressResult> results = repository.findAll();

        Map<String, List<SpecificAddressResult>> grouped = results.stream()
                .collect(Collectors.groupingBy(r -> r.getId().getGeocoderId()));

        return grouped.entrySet().stream()
                .map(e -> buildResponse(e.getKey(), e.getValue()))
                .toList();
    }

    private StatsResponse buildResponse(String provider, List<SpecificAddressResult> results) {
        List<StatsAddressResponse> entries = results.stream()
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

        List<Double> errors = entries.stream()
                .map(e -> e.errorMeters())
                .sorted()
                .toList();

        double avg = errors.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double max = errors.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double median = errors.size() % 2 == 1
                ? errors.get(errors.size() / 2)
                : (errors.get(errors.size() / 2 - 1) + errors.get(errors.size() / 2)) / 2.0;
        
        double porcentageErrors = (double) errors.stream().
                filter(e -> e > 13).count() / errors.size() * 100.0;


        int coverage = entries.size();


        int totalErrorsTypographic = results.stream()
                .map(r -> !r.getIsResult() && r.getDireccion().getTipoDireccion().equals(AddressType.ERROR) ? 1:0)
                .reduce(0, Integer::sum);
                
        int totalErrorsPermutation = results.stream()
                .map(r -> !r.getIsResult() && r.getDireccion().getTipoDireccion().equals(AddressType.PERMUTACION) ? 1:0)
                .reduce(0, Integer::sum);

        int totalErrorsRural = results.stream()
                .map(r -> !r.getIsResult() && !List.of("MONTEVIDEO", "CANELONES").contains(r.getDireccion().getDepartamento()) ? 1:0)
                .reduce(0, Integer::sum);

        int totalErrorsUrban = results.stream()
                .map(r -> !r.getIsResult() && List.of("MONTEVIDEO", "CANELONES").contains(r.getDireccion().getDepartamento()) ? 1:0)
                .reduce(0, Integer::sum);

        return new StatsResponse(
                provider, 
                results.size(), 
                new StatsAccuracyResponse(avg, max, median, porcentageErrors), 
                new StatsCoverageResponse(coverage), 
                new StatsReliabilityResponse(totalErrorsTypographic, totalErrorsPermutation, totalErrorsRural, totalErrorsUrban), 
                entries
        );
    }


}

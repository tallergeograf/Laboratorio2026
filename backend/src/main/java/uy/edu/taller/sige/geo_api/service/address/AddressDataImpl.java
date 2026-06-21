package uy.edu.taller.sige.geo_api.service.address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uy.edu.taller.sige.geo_api.client.GeoCoderFactory;
import uy.edu.taller.sige.geo_api.client.GeocoderSearchResult;
import uy.edu.taller.sige.geo_api.client.IGeoCoder;
import uy.edu.taller.sige.geo_api.dto.request.GeocodeRequestSearch;
import uy.edu.taller.sige.geo_api.dto.response.GeocodeResponse;
import uy.edu.taller.sige.geo_api.model.enums.GeocoderProvider;
import uy.edu.taller.sige.geo_api.model.Address;
import uy.edu.taller.sige.geo_api.model.SpecificAddress;
import uy.edu.taller.sige.geo_api.model.SpecificAddressResult;
import uy.edu.taller.sige.geo_api.model.SpecificAddressResultId;
import uy.edu.taller.sige.geo_api.model.enums.AddressCategory;
import uy.edu.taller.sige.geo_api.model.enums.AddressType;
import uy.edu.taller.sige.geo_api.repository.AddressJpaRepository;
import uy.edu.taller.sige.geo_api.repository.SpecificAddressJPARepository;
import uy.edu.taller.sige.geo_api.repository.SpecificAddressResultRepository;
import uy.edu.taller.sige.geo_api.utils.StreetAbbreviator;
import uy.edu.taller.sige.geo_api.utils.StreetMutator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

@Service
public class AddressDataImpl implements AddressDataService {

    private static final Logger log = LoggerFactory.getLogger(AddressDataImpl.class);

    private final AddressJpaRepository addresRepository;
    private final SpecificAddressJPARepository specificAddressRepository;
    private final SpecificAddressResultRepository specificAddressResultRepository;

    private List<String> initialAddresses;
    private final GeoCoderFactory geoCoderFactory;

    public AddressDataImpl(AddressJpaRepository repository, SpecificAddressJPARepository specificAddressRepository, SpecificAddressResultRepository specificAddressResultRepository, GeoCoderFactory geoCoderFactory) {
        this.addresRepository = repository;
        this.specificAddressRepository = specificAddressRepository;
        this.specificAddressResultRepository = specificAddressResultRepository;
        this.geoCoderFactory = geoCoderFactory;
        this.initialAddresses = new ArrayList<>(List.of(
                "842|MONTEVIDEO",
                "9060|MONTEVIDEO",
                "9207|MONTEVIDEO",
                "19422|MONTEVIDEO",
                "39938|MONTEVIDEO",
                "51401|MONTEVIDEO",
                "59872|MONTEVIDEO",
                "77020|MONTEVIDEO",
                "96701|MONTEVIDEO",
                "104355|MONTEVIDEO",
                "105791|MONTEVIDEO",
                "112328|MONTEVIDEO",
                "146479|MONTEVIDEO",
                "206917|MONTEVIDEO",
                "232980|MONTEVIDEO",
                "234205|MONTEVIDEO",
                "240991|MONTEVIDEO",
                "272552|MONTEVIDEO",
                "272559|MONTEVIDEO",
                "272561|MONTEVIDEO",
                "272586|MONTEVIDEO",
                "301217|MONTEVIDEO",
                "301718|MONTEVIDEO",
                "325264|MONTEVIDEO",
                "325289|MONTEVIDEO",
                "350121|MONTEVIDEO",
                "351510|MONTEVIDEO",
                "351762|MONTEVIDEO",
                "377415|MONTEVIDEO",
                "382686|MONTEVIDEO",
                "329490|MONTEVIDEO",
                "61626|MONTEVIDEO",
                "130891|MONTEVIDEO",
                "155406|MONTEVIDEO",
                "67158|MONTEVIDEO",
                "122719|MONTEVIDEO",
                "166677|MONTEVIDEO",
                "77717|MONTEVIDEO",
                "129011|MONTEVIDEO",
                "11920|MONTEVIDEO"
        ));
    }

    private static final int FULL_LIMIT = 50;
    private static final int DEMO_LIMIT = 3;
    private static final int BATCH_SIZE = 150;

    @Override
    public List<String> processAddress() {
        specificAddressResultRepository.deleteByIsDemo(false);
        specificAddressRepository.deleteByIsDemo(false);
        return process(false, new ArrayList<>(this.initialAddresses), FULL_LIMIT);
    }

    @Override
    public List<String> processAddressDemo() {
        specificAddressResultRepository.deleteByIsDemo(true);
        specificAddressRepository.deleteByIsDemo(true);
        return process(true, new ArrayList<>(), DEMO_LIMIT);
    }

    private List<String> process(boolean isDemo, List<String> ids, int lim) {
        if (!ids.isEmpty()) {
            List<Address> accuracyAddress = getbyId(ids);
            generateAndSave(accuracyAddress, AddressCategory.CALLE_NUMERO, AddressType.COMUN,
                    a -> a.getNombreVia() + " " + a.getNumPuerta(), isDemo);
            generateAndSaveAbbreviations(accuracyAddress, AddressCategory.CALLE_NUMERO,
                    a -> StreetAbbreviator.abbreviate(a.getNombreVia()) + " " + a.getNumPuerta(), isDemo);
            generateAndSave(accuracyAddress, AddressCategory.CALLE_NUMERO, AddressType.PERMUTACION,
                    a -> a.getNumPuerta() + " " + a.getNombreVia(), isDemo);
            generateAndSaveErrors(accuracyAddress, AddressCategory.CALLE_NUMERO,
                    a -> a.getNombreVia(), a -> " " + a.getNumPuerta(), isDemo);
        }

        // Native queries fail with NOT IN (null) when list is empty in PostgreSQL
        if (ids.isEmpty()) {
            ids.add("__NO_MATCH__");
        }

        List<Address> street = getStreetNumbers(ids, lim);
        generateAndSave(street, AddressCategory.CALLE_NUMERO, AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta(), isDemo);
        generateAndSave(street, AddressCategory.CALLE_NUMERO, AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " " + a.getNombreVia(), isDemo);
        generateAndSaveErrors(street, AddressCategory.CALLE_NUMERO,
                Address::getNombreVia, a -> " " + a.getNumPuerta(), isDemo);
        generateAndSaveAbbreviations(street, AddressCategory.CALLE_NUMERO,
                a -> StreetAbbreviator.abbreviate(a.getNombreVia()) + " " + a.getNumPuerta(), isDemo);
        addIdPoints(ids, street);

        List<Address> streetNumberLocality = getStreetNumberLocality(ids, lim);
        generateAndSave(streetNumberLocality, AddressCategory.CALLE_NUMERO_LOCALIDAD, AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta() + "," + a.getLocalidad(), isDemo);
        generateAndSaveAbbreviations(streetNumberLocality, AddressCategory.CALLE_NUMERO_LOCALIDAD,
                a -> StreetAbbreviator.abbreviate(a.getNombreVia()) + " " + a.getNumPuerta() + "," + a.getLocalidad(), isDemo);
        generateAndSave(streetNumberLocality, AddressCategory.CALLE_NUMERO_LOCALIDAD, AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " " + a.getNombreVia() + "," + a.getLocalidad(), isDemo);
        generateAndSaveErrors(streetNumberLocality, AddressCategory.CALLE_NUMERO_LOCALIDAD,
                Address::getNombreVia, a -> " " + a.getNumPuerta() + "," + a.getLocalidad(), isDemo);
        addIdPoints(ids, streetNumberLocality);

        List<Address> streetNumberDepartament = getStreetNumberDepartament(ids, lim);
        generateAndSave(streetNumberDepartament, AddressCategory.CALLE_NUMERO_DEPARTAMENTO, AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta() + "," + a.getDepartamento(), isDemo);
        generateAndSaveAbbreviations(streetNumberDepartament, AddressCategory.CALLE_NUMERO_DEPARTAMENTO,
                a -> StreetAbbreviator.abbreviate(a.getNombreVia()) + " " + a.getNumPuerta() + "," + a.getDepartamento(), isDemo);
        generateAndSave(streetNumberDepartament, AddressCategory.CALLE_NUMERO_DEPARTAMENTO, AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " " + a.getNombreVia() + "," + a.getDepartamento(), isDemo);
        generateAndSaveErrors(streetNumberDepartament, AddressCategory.CALLE_NUMERO_DEPARTAMENTO,
                Address::getNombreVia, a -> " " + a.getNumPuerta() + "," + a.getDepartamento(), isDemo);
        addIdPoints(ids, streetNumberDepartament);

        List<Address> streetNumberlocalityDepartament = getStreetNumberDepartamentlocality(ids, lim);
        generateAndSave(streetNumberlocalityDepartament, AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO, AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta() + "," + a.getLocalidad() + "," + a.getDepartamento(), isDemo);
        generateAndSaveAbbreviations(streetNumberlocalityDepartament, AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
                a -> StreetAbbreviator.abbreviate(a.getNombreVia()) + " " + a.getNumPuerta() + "," + a.getLocalidad() + "," + a.getDepartamento(), isDemo);
        generateAndSave(streetNumberlocalityDepartament, AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO, AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " " + a.getNombreVia() + "," + a.getLocalidad() + "," + a.getDepartamento(), isDemo);
        generateAndSaveErrors(streetNumberlocalityDepartament, AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
                Address::getNombreVia, a -> " " + a.getNumPuerta() + "," + a.getLocalidad() + "," + a.getDepartamento(), isDemo);
        addIdPoints(ids, streetNumberlocalityDepartament);

        List<Address> routeKilometer = getRouteKilometer(ids, lim);
        generateAndSave(routeKilometer, AddressCategory.RUTA_KILOMETRO, AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getKm(), isDemo);
        generateAndSaveErrors(routeKilometer, AddressCategory.RUTA_KILOMETRO,
                Address::getNombreVia, a -> " " + a.getKm(), isDemo);
        addIdPoints(ids, routeKilometer);

        List<Address> interestPoint = getInterestPoint(ids, lim);
        generateAndSave(interestPoint, AddressCategory.PUNTO_DE_INTERES, AddressType.COMUN,
                Address::getNombreInmueble, isDemo);
        generateAndSaveErrors(interestPoint, AddressCategory.PUNTO_DE_INTERES,
                Address::getNombreInmueble, a -> "", isDemo);
        addIdPoints(ids, interestPoint);

        List<Address> lotBlock = getLotBlock(ids, lim);
        generateAndSave(lotBlock, AddressCategory.SOLAR_MANZANA, AddressType.COMUN,
                a -> "Solar " + a.getSolar() + " Manzana " + a.getManzana(), isDemo);
        generateAndSave(lotBlock, AddressCategory.SOLAR_MANZANA, AddressType.PERMUTACION,
                a -> "Manzana " + a.getManzana() + " Solar " + a.getSolar(), isDemo);
        generateAndSaveErrors(lotBlock, AddressCategory.SOLAR_MANZANA,
                a -> Math.abs((a.getSolar() + "-" + a.getManzana()).hashCode()) % 2 == 0 ? "Solar" : "Manzana",
                a -> Math.abs((a.getSolar() + "-" + a.getManzana()).hashCode()) % 2 == 0
                        ? " " + a.getSolar() + " Manzana " + a.getManzana()   
                        : " " + a.getManzana() + " Solar " + a.getSolar(),  
                isDemo);
        addIdPoints(ids, lotBlock);
        List<Address> intersectionsAddresses=getIntersectionsAddresses(lim);

        generateAndSave(intersectionsAddresses, AddressCategory.INTERSECCION_CALLE, AddressType.COMUN,
                Address::getNombreVia, isDemo);

        generateAndSaveErrors(intersectionsAddresses, AddressCategory.INTERSECCION_CALLE,
                Address::getNombreVia, a -> "", isDemo);
        addIdPoints(ids, intersectionsAddresses);
        geocoderProcces(isDemo);
        return ids.stream().filter(id -> !id.equals("__NO_MATCH__")).toList();
    }

    private void geocoderProcces(boolean isDemo) {
        IGeoCoder geoPhoton    = geoCoderFactory.getGeoCoder(GeocoderProvider.PHOTON);
        IGeoCoder geoSudir     = geoCoderFactory.getGeoCoder(GeocoderProvider.SUDIR);
        IGeoCoder geoNominatim = geoCoderFactory.getGeoCoder(GeocoderProvider.NOMINATIM);

        List<GeocodeRequestSearch> addresses = specificAddressRepository.findByIsDemo(isDemo)
                .stream()
                .map(x -> new GeocodeRequestSearch(
                        x.getId(),
                        x.getFullAddress(),
                        x.getDepartamento(),
                        x.getTipoDireccion(),
                        x.getCategoria()))
                .toList();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        Future<?> f1 = executor.submit(() -> processBatches(geoPhoton,    "photon",    addresses, isDemo));
        Future<?> f2 = executor.submit(() -> processBatches(geoSudir,     "sudir",     addresses, isDemo));
        Future<?> f3 = executor.submit(() -> processBatches(geoNominatim, "nominatim", addresses, isDemo));

        try { f1.get(); } catch (Exception e) { log.error("photon error: {}",    e.getMessage()); }
        try { f2.get(); } catch (Exception e) { log.error("sudir error: {}",     e.getMessage()); }
        try { f3.get(); } catch (Exception e) { log.error("nominatim error: {}", e.getMessage()); }

        executor.shutdown();
    }

    private void processBatches(IGeoCoder geocoder, String providerId, List<GeocodeRequestSearch> addresses, boolean isDemo) {
        int total = (int) Math.ceil((double) addresses.size() / BATCH_SIZE);
        for (int i = 0; i < addresses.size(); i += BATCH_SIZE) {
            List<GeocodeRequestSearch> batch = addresses.subList(i, Math.min(i + BATCH_SIZE, addresses.size()));
            log.info("[{}] batch {}/{} ({} addresses)", providerId, i / BATCH_SIZE + 1, total, batch.size());
            try {
                List<GeocoderSearchResult> results = geocoder.searchBatch(batch);
                List<SpecificAddressResult> toSave = new ArrayList<>();
                for (int j = 0; j < batch.size(); j++)
                    toSave.add(buildResult(providerId, results.get(j), batch.get(j), isDemo));
                specificAddressResultRepository.saveAll(toSave);
                log.info("[{}] batch {}/{} saved", providerId, i / BATCH_SIZE + 1, total);
            } catch (Exception e) {
                log.error("[{}] batch {} failed: {}", providerId, i / BATCH_SIZE + 1, e.getMessage());
            }
        }
    }

    private SpecificAddressResult buildResult(String providerId, GeocoderSearchResult searchResult, GeocodeRequestSearch oneAddress, boolean isDemo) {
        SpecificAddressResult entity = new SpecificAddressResult();
        entity.setDireccion(specificAddressRepository.getReferenceById(oneAddress.id()));
        entity.setStatusCode(searchResult.statusCode());

        if (searchResult.results().isEmpty()) {
            entity.setId(new SpecificAddressResultId(oneAddress.id(), providerId, isDemo));
            entity.setIsResult(false);
        } else {
            GeocodeResponse first = searchResult.results().get(0);
            entity.setId(new SpecificAddressResultId(oneAddress.id(), first.source(), isDemo));
            entity.setLatitud(first.lat());
            entity.setLongitud(first.lon());
            entity.setIsResult(true);
            entity.setLatencia(first.latencyMs());
        }

        return entity;
    }

    private List<Address> getStreetNumbers(List<String> ids, int lim) {
        return new ArrayList<>(addresRepository.findStreetNumber(ids, lim));
    }

    private List<Address> getStreetNumberLocality(List<String> ids, int lim) {
        return new ArrayList<>(addresRepository.findStreetNumberLocality(ids, lim));
    }

    private List<Address> getStreetNumberDepartament(List<String> ids, int lim) {
        return new ArrayList<>(addresRepository.findStreetNumberDepartament(ids, lim));
    }

    private List<Address> getStreetNumberDepartamentlocality(List<String> ids, int lim) {
        return new ArrayList<>(addresRepository.findStreetNumberDepartamentlocality(ids, lim));
    }

    private List<Address> getRouteKilometer(List<String> ids, int lim) {
        return new ArrayList<>(addresRepository.findRouteKilometer(ids, lim));
    }

    private List<Address> getInterestPoint(List<String> ids, int lim) {
        return new ArrayList<>(addresRepository.findInterestPoint(ids, lim));
    }
    private List<Address> getIntersectionsAddresses (int lim){
        return new ArrayList<>(
                addresRepository.findByAddressIdContaining(
                        "MONTEVIDEO_CRUCE",
                        PageRequest.of(0, lim)
                )
        );
    }

    private List<Address> getLotBlock(List<String> ids, int lim) {
        return new ArrayList<>(addresRepository.findLotBlock(ids, lim));
    }

    private List<Address> getbyId(List<String> ids) {
        return new ArrayList<>(addresRepository.findAllById(ids));
    }

    /*
    HELPERS
    */
    private void addIdPoints(List<String> ids, List<Address> addresses) {
        ids.addAll(addresses.stream().map(Address::getAddressId).toList());
    }

    private List<SpecificAddress> toSpecificAddresses(
            List<Address> addresses,
            AddressCategory categoria,
            AddressType tipoDireccion,
            Function<Address, String> addressBuilder,
            boolean isDemo
    ) {
        return addresses.stream()
                .map(a -> {
                    SpecificAddress d = new SpecificAddress();
                    d.setDepartamento(a.getDepartamento());
                    d.setFullAddress(addressBuilder.apply(a));
                    d.setCategoria(categoria);
                    d.setTipoDireccion(tipoDireccion);
                    d.setLatitud(a.getLatitud());
                    d.setLongitud(a.getLongitud());
                    d.setIsDemo(isDemo);
                    return d;
                })
                .toList();
    }

    private void generateAndSave(
            List<Address> source,
            AddressCategory category,
            AddressType type,
            Function<Address, String> addressBuilder,
            boolean isDemo
    ) {
        specificAddressRepository.saveAll(toSpecificAddresses(source, category, type, addressBuilder, isDemo));
    }

    private void generateAndSaveAbbreviations(
            List<Address> source,
            AddressCategory category,
            Function<Address, String> addressBuilder,
            boolean isDemo
    ) {
        List<SpecificAddress> addresses = source.stream()
                .filter(a -> StreetAbbreviator.abbreviate(a.getNombreVia()) != null)
                .map(a -> {
                    SpecificAddress d = new SpecificAddress();
                    d.setDepartamento(a.getDepartamento());
                    d.setFullAddress(addressBuilder.apply(a));
                    d.setCategoria(category);
                    d.setTipoDireccion(AddressType.ABREVIACION);
                    d.setLatitud(a.getLatitud());
                    d.setLongitud(a.getLongitud());
                    d.setIsDemo(isDemo);
                    return d;
                })
                .toList();

        specificAddressRepository.saveAll(addresses);
    }

    private void generateAndSaveErrors(
            List<Address> source,
            AddressCategory category,
            Function<Address, String> partToMutate,
            Function<Address, String> suffix,
            boolean isDemo
    ) {
        List<SpecificAddress> addresses = source.stream()
                .map(a -> {
                    StreetMutator.MutationResult mutation = StreetMutator.mutateWithType(partToMutate.apply(a));
                    SpecificAddress d = new SpecificAddress();
                    d.setDepartamento(a.getDepartamento());
                    d.setFullAddress(mutation.mutated() + suffix.apply(a));
                    d.setCategoria(category);
                    d.setTipoDireccion(mutation.type());
                    d.setLatitud(a.getLatitud());
                    d.setLongitud(a.getLongitud());
                    d.setIsDemo(isDemo);
                    return d;
                })
                .toList();

        specificAddressRepository.saveAll(addresses);
    }
}
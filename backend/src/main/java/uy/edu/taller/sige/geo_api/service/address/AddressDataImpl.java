package uy.edu.taller.sige.geo_api.service.address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uy.edu.taller.sige.geo_api.client.GeoCoderFactory;
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
    public AddressDataImpl(AddressJpaRepository repository,SpecificAddressJPARepository specificAddressRepository,SpecificAddressResultRepository specificAddressResultRepository,GeoCoderFactory geoCoderFactory) {

        this.addresRepository = repository;
        this.specificAddressRepository=specificAddressRepository;
        this.specificAddressResultRepository=specificAddressResultRepository;
        this.geoCoderFactory=geoCoderFactory;
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
    @Override
    public List<String> processAddress() {
        specificAddressResultRepository.deleteAll();
        specificAddressRepository.deleteAll();
        // TODO: resolve abreviacion
        List<Address> accuracyAddress = getbyId(this.initialAddresses);
        generateAndSave(
                accuracyAddress,
                AddressCategory.CALLE_NUMERO,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta()
        );
        
        generateAndSaveAbbreviations(
                accuracyAddress,
                AddressCategory.CALLE_NUMERO,
                a -> StreetAbbreviator.abbreviate(a.getNombreVia())
                        + " " + a.getNumPuerta()
        );

        generateAndSave(
                accuracyAddress,
                AddressCategory.CALLE_NUMERO,
                AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " " + a.getNombreVia()
        );

        generateAndSave(
                accuracyAddress,
                AddressCategory.CALLE_NUMERO,
                AddressType.ERROR,
                a -> StreetMutator.mutate(a.getNombreVia()) + " " + a.getNumPuerta()
        );


        List<Address> street = getStreetNumbers(this.initialAddresses);
        generateAndSave(
                street,
                AddressCategory.CALLE_NUMERO,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta()
        );

        generateAndSave(
                street,
                AddressCategory.CALLE_NUMERO,
                AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " " + a.getNombreVia()
        );

        generateAndSave(
                street,
                AddressCategory.CALLE_NUMERO,
                AddressType.ERROR,
                a ->  StreetMutator.mutate(a.getNombreVia()) + " " +a.getNumPuerta()
        );

        generateAndSaveAbbreviations(
                street,
                AddressCategory.CALLE_NUMERO,
                a -> StreetAbbreviator.abbreviate(a.getNombreVia())
                        + " " + a.getNumPuerta()
        );
        addIdPoints(street);

        List<Address> streetNumberLocality = getStreetNumberLocality(this.initialAddresses);

        generateAndSave(
                streetNumberLocality,
                AddressCategory.CALLE_NUMERO_LOCALIDAD,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta()+ "," +a.getLocalidad()
        );

        generateAndSaveAbbreviations(
                streetNumberLocality,
                AddressCategory.CALLE_NUMERO_LOCALIDAD,
                a -> StreetAbbreviator.abbreviate(a.getNombreVia())
                        + " " + a.getNumPuerta()
                        + "," + a.getLocalidad()
        );

        generateAndSave(
                streetNumberLocality,
                AddressCategory.CALLE_NUMERO_LOCALIDAD,
                AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " "+a.getNombreVia()  + "," +a.getLocalidad()
        );

        generateAndSave(
                streetNumberLocality,
                AddressCategory.CALLE_NUMERO_LOCALIDAD,
                AddressType.ERROR,
                a -> StreetMutator.mutate(a.getNombreVia()) + " " + a.getNumPuerta()+ "," +a.getLocalidad()
        );
        addIdPoints(streetNumberLocality);

        List<Address> streetNumberDepartament = getStreetNumberDepartament(this.initialAddresses);
        generateAndSave(
                streetNumberDepartament,
                AddressCategory.CALLE_NUMERO_DEPARTAMENTO,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta()+ "," +a.getDepartamento()
        );

        generateAndSaveAbbreviations(
                streetNumberDepartament,
                AddressCategory.CALLE_NUMERO_DEPARTAMENTO,
                a -> StreetAbbreviator.abbreviate(a.getNombreVia())
                        + " " + a.getNumPuerta()
                        + "," + a.getDepartamento()
        );

        generateAndSave(
                streetNumberDepartament,
                AddressCategory.CALLE_NUMERO_DEPARTAMENTO,
                AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " "+a.getNombreVia()  + "," +a.getDepartamento()
        );

        generateAndSave(
                streetNumberDepartament,
                AddressCategory.CALLE_NUMERO_DEPARTAMENTO,
                AddressType.ERROR,
                a -> StreetMutator.mutate(a.getNombreVia()) + " " + a.getNumPuerta()+ "," +a.getDepartamento()
        );
        addIdPoints(streetNumberDepartament);
        List<Address> streetNumberlocalityDepartament = getStreetNumberDepartamentlocality(this.initialAddresses);
        generateAndSave(
                streetNumberlocalityDepartament,
                AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta()+ ","+a.getLocalidad()+"," +a.getDepartamento()
        );

        generateAndSaveAbbreviations(
                streetNumberlocalityDepartament,
                AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
                a -> StreetAbbreviator.abbreviate(a.getNombreVia())
                        + " " + a.getNumPuerta()
                        + "," + a.getLocalidad()
                        + "," + a.getDepartamento()
        );

        generateAndSave(
                streetNumberlocalityDepartament,
                AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
                AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " " + a.getNombreVia()+ ","+a.getLocalidad()+"," +a.getDepartamento()
        );

        generateAndSave(
                streetNumberlocalityDepartament,
                AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
                AddressType.ERROR,
                a -> StreetMutator.mutate(a.getNombreVia()) + " " + a.getNumPuerta()+ ","+a.getLocalidad()+"," +a.getDepartamento()
        );
        addIdPoints(streetNumberlocalityDepartament);
        List<Address> routeKilometer = getRouteKilometer(this.initialAddresses);
        generateAndSave(
                routeKilometer,
                AddressCategory.RUTA_KILOMETRO,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getKm()
        );
       
        generateAndSave(
                routeKilometer,
                AddressCategory.RUTA_KILOMETRO,
                AddressType.ERROR,
                a -> StreetMutator.mutate(a.getNombreVia()) + " " + a.getKm()
        );
        addIdPoints(routeKilometer);

        List<Address> interestPoint = getInterestPoint(this.initialAddresses);
        generateAndSave(
                interestPoint,
                AddressCategory.PUNTO_DE_INTERES,
                AddressType.COMUN,
                Address::getNombreInmueble
        );
        
        generateAndSave(
                interestPoint,
                AddressCategory.PUNTO_DE_INTERES,
                AddressType.ERROR,
                a -> StreetMutator.mutate(a.getNombreInmueble())
        );
        addIdPoints(interestPoint);

        List<Address> lotBlock = getLotBlock(this.initialAddresses);
        generateAndSave(
                lotBlock,
                AddressCategory.SOLAR_MANZANA,
                AddressType.COMUN,
                a -> "Solar "+a.getSolar()+" Manzana "+ a.getManzana()
        );
        
        generateAndSave(
                lotBlock,
                AddressCategory.SOLAR_MANZANA,
                AddressType.PERMUTACION,
                a -> "Manzana "+ a.getManzana()+ " Solar "+a.getSolar()
        );
        generateAndSave(
                lotBlock,
                AddressCategory.SOLAR_MANZANA,
                AddressType.ERROR,
                a -> {
                    boolean mutateSolar =
                            Math.abs((a.getSolar() + "-" + a.getManzana()).hashCode()) % 2 == 0;

                    return mutateSolar
                            ? StreetMutator.mutate("Solar") + " " + a.getSolar()
                            + " Manzana " + a.getManzana()
                            : "Solar " + a.getSolar()
                            + " " + StreetMutator.mutate("Manzana")
                            + " " + a.getManzana();
                }
        );
        addIdPoints(lotBlock);
        //geocoderProcces();
        return this.initialAddresses;

        // 2) Llamar a cada geocoder con las direcciones yobtener resultado
        // 3) Guardar en la tabla

    }
    private void geocoderProcces() {
        IGeoCoder geoPhoton    = geoCoderFactory.getGeoCoder(GeocoderProvider.PHOTON);
        IGeoCoder geoSudir     = geoCoderFactory.getGeoCoder(GeocoderProvider.SUDIR);
        IGeoCoder geoNominatim = geoCoderFactory.getGeoCoder(GeocoderProvider.NOMINATIM);

        List<GeocodeRequestSearch> addresses = specificAddressRepository.findAll()
                .stream()
                .map(x -> new GeocodeRequestSearch(
                        x.getId(),
                        x.getFullAddress(),
                        x.getDepartamento(),
                        x.getTipoDireccion(),
                        x.getCategoria()))
                .toList();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        Future<?> f1 = executor.submit(() -> {
                for (GeocodeRequestSearch oneAddress : addresses) {
                trySearch(geoPhoton, "photon", oneAddress);
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
        });

        Future<?> f2 = executor.submit(() -> {
                for (GeocodeRequestSearch oneAddress : addresses) {
                trySearch(geoSudir, "sudir", oneAddress);
                }
        });

        Future<?> f3 = executor.submit(() -> {
                for (GeocodeRequestSearch oneAddress : addresses) {
                trySearch(geoNominatim, "nominatim", oneAddress);
                try { Thread.sleep(1100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
        });

        try { f1.get(); } catch (Exception e) { log.error("photon error: {}", e.getMessage()); }
        try { f2.get(); } catch (Exception e) { log.error("sudir error: {}", e.getMessage()); }
        try { f3.get(); } catch (Exception e) { log.error("nominatim error: {}", e.getMessage()); }

        executor.shutdown();
    }

    private void trySearch(IGeoCoder geocoder, String providerId, GeocodeRequestSearch oneAddress) {
        try {
            saveFirstResult(providerId, geocoder.search(oneAddress), oneAddress);
        } catch (Exception e) {
            log.warn("Geocoder {} failed for address id={}: {}", providerId, oneAddress.id(), e.getMessage());
        }
    }

    private void saveFirstResult(String providerId, List<GeocodeResponse> results, GeocodeRequestSearch oneAddress) {
        SpecificAddressResult entity = new SpecificAddressResult();
        entity.setDireccion(specificAddressRepository.getReferenceById(oneAddress.id()));

        if (results.isEmpty()) {
            entity.setId(new SpecificAddressResultId(oneAddress.id(), providerId));
            entity.setIsResult(false);
        } else {
            GeocodeResponse first = results.get(0);
            entity.setId(new SpecificAddressResultId(oneAddress.id(), first.source()));
            entity.setLatitud(first.lat());
            entity.setLongitud(first.lon());
            entity.setIsResult(true);
            entity.setLatencia(first.latencyMs());
        }

        specificAddressResultRepository.save(entity);
    }
    private List<Address> getStreetNumbers(List<String> initialAddresses) {

        return new ArrayList<>(addresRepository.findStreetNumber(initialAddresses));
    }
    private List<Address> getStreetNumberLocality(List<String> initialAddresses) {

        return new ArrayList<>(addresRepository.findStreetNumberLocality(initialAddresses));
    }
    private List<Address> getStreetNumberDepartament(List<String> initialAddresses) {

        return new ArrayList<>(addresRepository.findStreetNumberDepartament(initialAddresses));
    }
    private List<Address> getStreetNumberDepartamentlocality(List<String> initialAddresses) {

        return new ArrayList<>(addresRepository.findStreetNumberDepartamentlocality(initialAddresses));
    }
    private List<Address> getRouteKilometer(List<String> initialAddresses) {
        return new ArrayList<>(addresRepository.findRouteKilometer(initialAddresses));
    }
    private List<Address>getInterestPoint(List<String> initialAddresses){
        return new ArrayList<>(addresRepository.findInterestPoint(initialAddresses));
    }
    private List<Address> getLotBlock(List<String> initialAddresses) {

        return new ArrayList<>(addresRepository.findLotBlock(initialAddresses));
    }

    private List<Address> getbyId( List<String> initialAddresses) {

        return new ArrayList<>(addresRepository.findAllById(initialAddresses));
    }
    /*
    HELPERS
    */
    private void addIdPoints(List<Address> addresses) {
        this.initialAddresses.addAll(
                addresses.stream()
                        .map(Address::getAddressId)
                        .toList()
        );
    }
    private List<SpecificAddress> toSpecificAddresses(
            List<Address> addresses,
            AddressCategory categoria,
            AddressType tipoDireccion,
            Function<Address, String> addressBuilder
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
                return d;
            })
            .toList();
    }
    private void generateAndSave(
            List<Address> source,
            AddressCategory category,
            AddressType type,
            Function<Address, String> addressBuilder
    ) {
        List<SpecificAddress> addresses = toSpecificAddresses(
                source,
                category,
                type,
                addressBuilder
        );

        specificAddressRepository.saveAll(addresses);
    }

    private void generateAndSaveAbbreviations(
                List<Address> source,
                AddressCategory category,
                Function<Address, String> addressBuilder
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
                        return d;
                })
                .toList();

        specificAddressRepository.saveAll(addresses);
      }
}

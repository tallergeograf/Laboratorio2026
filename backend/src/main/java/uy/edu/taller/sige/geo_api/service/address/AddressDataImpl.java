package uy.edu.taller.sige.geo_api.service.address;

import org.springframework.stereotype.Service;
import uy.edu.taller.sige.geo_api.model.Address;
import uy.edu.taller.sige.geo_api.model.SpecificAddress;
import uy.edu.taller.sige.geo_api.model.enums.AddressCategory;
import uy.edu.taller.sige.geo_api.model.enums.AddressType;
import uy.edu.taller.sige.geo_api.repository.AddressJpaRepository;
import uy.edu.taller.sige.geo_api.repository.SpecificAddressJPARepository;
import uy.edu.taller.sige.geo_api.utils.StreetMutator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@Service
public class AddressDataImpl implements AddressDataService {

    private final AddressJpaRepository addresRepository;
    private final SpecificAddressJPARepository specificAddressRepository;
    private  List<String>initialWKBPoint;
    public AddressDataImpl(AddressJpaRepository repository,SpecificAddressJPARepository specificAddressRepository) {

        this.addresRepository = repository;
        this.specificAddressRepository=specificAddressRepository;
        this.initialWKBPoint = new ArrayList<>(List.of(
                "0101000020D17F000085EB51B8CFA32141E17A14BE53735741",
                "0101000020D17F000085EB51B8D2A3214185EB51C850735741",
                "0101000020D17F00001F85EB511593214185EB5148026E5741",
                "0101000020D17F0000E6652AED59AD2141FEA141E2EE725741",
                "0101000020D17F00000AD7A3F05FA92141E17A14CED2725741",
                "0101000020D17F00000AD7A3709FA921410AD7A3D0FC725741",
                "0101000020D17F0000C3F528DC98AA21415C8FC2557A735741",
                "0101000020D17F00005C8FC2F5D4A32141295C8F924E735741",
                "0101000020D17F000052B81E05DBA321419A99999948735741",
                "0101000020D17F000005F6F6B5ACDE21417B03A14F2F6A5741",
                "0101000020D17F0000EC51B89EDE912141C3F528DCF7685741",
                "0101000020D17F0000EC51B81EDBCF214185EB5198B26A5741",
                "0101000020D17F00004EC3C3FF7CD221414B4B1638D26B5741",
                "0101000020D17F0000A4703D8A5B8C214100000050056F5741",
                "0101000020D17F0000A4703D8A60C721417B14AE57746A5741",
                "0101000020D17F000000000000AFDC2141CDCCCC2C8A6A5741",
                "0101000020D17F00004670DC05C5C321416C641ED6E66A5741",
                "0101000020D17F00002CDA30C9E5C62141176F393BD76A5741",
                "0101000020D17F0000CDCCCC4CCEC921413D0AD7A3896A5741",
                "0101000020D17F00004185507B729521411407B3ECE6675741",
                "0101000020D17F00005C8FC2750A972141AE47E11A17695741",
                "0101000020D17F000048E17A1464932141B81E85AB7D6D5741",
                "0101000020D17F00001F85EB51DA7C2141D7A370BD4F6F5741",
                "0101000020D17F0000F6285C0F877B2141D7A370AD816F5741",
                "0101000020D17F0000E17A14AE617F214185EB51D8136F5741",
                "0101000020D17F00007B14AEC77D812141713D0A67E46A5741",
                "0101000020D17F00000AD7A37013A92141E17A142ECA685741",
                "0101000020D17F000066666666D7AB2141F6285C1F88695741",
                "0101000020D17F0000997DE92919B8214110142CAE326B5741"
        ));

    }
    @Override
    public List<String> processAddress() {
        specificAddressRepository.deleteAll();
        // TODO: resolve abreviacion
        // TODO: RESOLVER DIRECCIONES REPETIDAS
        List<Address> accuracyAddress = getbyWkt(this.initialWKBPoint);
        generateAndSave(
                accuracyAddress,
                AddressCategory.CALLE_NUMERO,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta()
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



        List<Address> street = getStreetNumbers(this.initialWKBPoint);
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
        addWkbPoints(street);

        List<Address> streetNumberLocality = getStreetNumberLocality(this.initialWKBPoint);

        generateAndSave(
                streetNumberLocality,
                AddressCategory.CALLE_NUMERO_LOCALIDAD,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta()+ "," +a.getLocalidad()
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
        addWkbPoints(streetNumberLocality);

        List<Address> streetNumberDepartament = getStreetNumberDepartament(this.initialWKBPoint);
        generateAndSave(
                streetNumberDepartament,
                AddressCategory.CALLE_NUMERO_DEPARTAMENTO,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta()+ "," +a.getDepartamento()
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
        addWkbPoints(streetNumberDepartament);
        List<Address> streetNumberlocalityDepartament = getStreetNumberDepartamentlocality(this.initialWKBPoint);
        generateAndSave(
                streetNumberlocalityDepartament,
                AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getNumPuerta()+ ","+a.getLocalidad()+"," +a.getDepartamento()
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
        addWkbPoints(streetNumberlocalityDepartament);
        List<Address> routeKilometer = getRouteKilometer(this.initialWKBPoint);
        generateAndSave(
                routeKilometer,
                AddressCategory.RUTA_KILOMETRO,
                AddressType.COMUN,
                a -> a.getNombreVia() + " " + a.getKm()
        );
        //TODO: CONSULTAR
        /*generateAndSave(
                routeKilometer,
                AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
                AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " " + a.getNombreVia()+ ","+a.getLocalidad()+"," +a.getDepartamento()
        );*/
        generateAndSave(
                routeKilometer,
                AddressCategory.RUTA_KILOMETRO,
                AddressType.ERROR,
                a -> StreetMutator.mutate(a.getNombreVia()) + " " + a.getKm()
        );
        addWkbPoints(routeKilometer);

        List<Address> interestPoint = getInterestPoint(this.initialWKBPoint);
        generateAndSave(
                interestPoint,
                AddressCategory.PUNTO_DE_INTERES,
                AddressType.COMUN,
                Address::getNombreInmueble
        );
        //TODO: CONSULTAR
        /*generateAndSave(
                interestPoint,
                AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
                AddressType.PERMUTACION,
                a -> a.getNumPuerta() + " " + a.getNombreVia()+ ","+a.getLocalidad()+"," +a.getDepartamento()
        );*/
        generateAndSave(
                interestPoint,
                AddressCategory.PUNTO_DE_INTERES,
                AddressType.ERROR,
                a -> StreetMutator.mutate(a.getNombreInmueble())
        );
        addWkbPoints(interestPoint);

        List<Address> lotBlock = getLotBlock(this.initialWKBPoint);
        generateAndSave(
                lotBlock,
                AddressCategory.SOLAR_MANZANA,
                AddressType.COMUN,
                a -> "Solar "+a.getSolar()+" Manzana "+ a.getManzana()
        );
        //TODO: CONSULTAR
        generateAndSave(
                lotBlock,
                AddressCategory.CALLE_NUMERO_LOCALIDAD_DEPARTAMENTO,
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
        addWkbPoints(lotBlock);

        return this.initialWKBPoint;

        //
        // 1.1) Generar las variaciones, permutacion, error, abreviacion
        // 1.2) Guardar direcciones en la tabla
        // 2) Llamar a cada geocoder con las direcciones yobtener resultado
        // 3) Guardar en la tabla

    }
    private List<Address> getStreetNumbers(List<String> initialWKBPoint) {

        return new ArrayList<>(addresRepository.findStreetNumber(initialWKBPoint));
    }
    private List<Address> getStreetNumberLocality(List<String> initialWKBPoint) {

        return new ArrayList<>(addresRepository.findStreetNumberLocality(initialWKBPoint));
    }
    private List<Address> getStreetNumberDepartament(List<String> initialWKBPoint) {

        List<Address> result = new ArrayList<>(addresRepository.findStreetNumberDepartament(initialWKBPoint));

        return result;
    }
    private List<Address> getStreetNumberDepartamentlocality(List<String> initialWKBPoint) {

        return new ArrayList<>(addresRepository.findStreetNumberDepartamentlocality(initialWKBPoint));
    }
    private List<Address> getRouteKilometer(List<String> initialWKBPoint) {
        return new ArrayList<>(addresRepository.findRouteKilometer(initialWKBPoint));
    }
    private List<Address>getInterestPoint(List<String> initialWKBPoint){
        return new ArrayList<>(addresRepository.findInterestPoint(initialWKBPoint));
    }
    private List<Address> getLotBlock(List<String> initialWKBPoint) {

        return new ArrayList<>(addresRepository.findLotBlock(initialWKBPoint));
    }

    private List<Address> getbyWkt( List<String> accuracyAddress) {

        return new ArrayList<>(addresRepository.findByWkb(accuracyAddress));
    }
    /*
    HELPERS
    */
    private void addWkbPoints(List<Address> addresses) {
        this.initialWKBPoint.addAll(
                addresses.stream()
                        .map(Address::getPuntoWkb)
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
}
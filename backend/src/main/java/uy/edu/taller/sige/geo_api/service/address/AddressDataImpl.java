package uy.edu.taller.sige.geo_api.service.address;

import org.springframework.stereotype.Service;
import uy.edu.taller.sige.geo_api.model.Address;
import uy.edu.taller.sige.geo_api.repository.AddressJpaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressDataImpl implements AddressDataService {

    private final AddressJpaRepository repository;

    public AddressDataImpl(AddressJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Address> processAddress() {

        List<Address> street=getLotBlock();
        return street;
        // 1) Traer las direcciones por cateogrias (criterico de eleccion)
        // 1.1) Generar las variaciones, permutacion, error, abreviacion
        // 1.2) Guardar direcciones en la tabla
        // 2) Llamar a cada geocoder con las direcciones yobtener resultado
        // 3) Guardar en la tabla

    }
    private List<Address> getStreetNumbers() {

        List<Address> result = new ArrayList<>(repository.findStreetNumber());

        return result;
    }

    private List<Address> getStreetNumberLocality() {

        List<Address> result = new ArrayList<>(repository.findStreetNumberLocality());

        return result;
    }

    private List<Address> getStreetNumberDepartament() {

        List<Address> result = new ArrayList<>(repository.findStreetNumberDepartament());

        return result;
    }

    private List<Address> getLotBlock() {

        List<Address> result = new ArrayList<>(repository.findLotBlock());

        return result;
    }

    private List<Address> getbyWkt() {

        List<Address> result = new ArrayList<>(repository.findByWkb());

        return result;
    }

}
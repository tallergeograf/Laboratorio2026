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

        List<Address> street=getStreetNumbers();
        List<Address> streetNumberDepartamentlocality=getStreetNumberDepartamentlocality();
        List<Address> routeKilometer=getRouteKilometer();
        List<Address> interestPoint=getInterestPoint();
        return interestPoint;
        // 1) Traer las direcciones por cateogrias (criterico de eleccion)
        // 1.1) Generar las variaciones, permutacion, error, abreviacion
        // 1.2) Guardar direcciones en la tabla
        // 2) Llamar a cada geocoder con las direcciones yobtener resultado
        // 3) Guardar en la tabla

    }
    private List<Address> getStreetNumbers() {

        return new ArrayList<>(repository.findStreetNumber());
    }
    private List<Address> getStreetNumberDepartamentlocality() {

        return new ArrayList<>(repository.findStreetNumberDepartamentlocality());
    }
    private List<Address> getRouteKilometer() {
        return new ArrayList<>(repository.findRouteKilometer());
    }
    private List<Address>getInterestPoint(){
        return new ArrayList<>(repository.findInterestPoint());
    }



}
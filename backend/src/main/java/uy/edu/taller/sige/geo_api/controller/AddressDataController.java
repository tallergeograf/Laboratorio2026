package uy.edu.taller.sige.geo_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uy.edu.taller.sige.geo_api.model.Address;
import uy.edu.taller.sige.geo_api.service.address.AddressDataService;

import java.util.List;

@RestController
@RequestMapping("/api/gis")
public class AddressDataController {

    private final AddressDataService addressDataService;

    public AddressDataController(AddressDataService addressDataService, AddressDataService addressDataService1) {
        this.addressDataService = addressDataService;
    }

    @GetMapping("/process")
    //dto
    public List<Address> processAddress() {

        return this.addressDataService.processAddress();
    }
}
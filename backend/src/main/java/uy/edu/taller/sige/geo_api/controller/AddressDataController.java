package uy.edu.taller.sige.geo_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uy.edu.taller.sige.geo_api.service.address.AddressDataService;

import java.util.List;

@RestController
@RequestMapping("/api/gis")
public class AddressDataController {

    private final AddressDataService addressDataService;

    public AddressDataController(AddressDataService addressDataService) {
        this.addressDataService = addressDataService;
    }

    @GetMapping("/process")
    public List<String> processAddress(@RequestParam(required = false, defaultValue = "false") boolean demo) {
        if (demo) {
            return this.addressDataService.processAddressDemo();
        }
        return this.addressDataService.processAddress();
    }
}
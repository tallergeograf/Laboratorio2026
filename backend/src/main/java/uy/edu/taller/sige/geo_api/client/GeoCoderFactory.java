package uy.edu.taller.sige.geo_api.client;

import java.util.Map;

import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.client.nominatim.NominatimClient;
import uy.edu.taller.sige.geo_api.client.photon.PhotonClient;
import uy.edu.taller.sige.geo_api.client.sudir.SudirClient;
import uy.edu.taller.sige.geo_api.model.enums.GeocoderProvider;

@Component
public class GeoCoderFactory {

    private final Map<GeocoderProvider, IGeoCoder> geocoders;

    public GeoCoderFactory(NominatimClient nominatim, PhotonClient photon, SudirClient sudir) {
        this.geocoders = Map.of(
            GeocoderProvider.NOMINATIM, nominatim,
            GeocoderProvider.PHOTON, photon,
            GeocoderProvider.SUDIR, sudir
        );
    }

    public IGeoCoder getGeoCoder(GeocoderProvider provider) {
        return geocoders.get(provider);
    }
}

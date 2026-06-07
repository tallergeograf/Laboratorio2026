package uy.edu.taller.sige.geo_api.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.enums.GeocoderProvider;

@Component
public class GeocoderProviderConverter implements Converter<String, GeocoderProvider> {

    @Override
    public GeocoderProvider convert(String source) {
        return GeocoderProvider.fromValue(source);
    }
}

package uy.edu.taller.sige.geo_api.enums;

public enum GeocoderProvider {
    NOMINATIM("nominatim"),
    PHOTON("photon"),
    SUDIR("sudir");

    private final String value;

    GeocoderProvider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static GeocoderProvider fromValue(String value) {
        for (GeocoderProvider p : values()) {
            if (p.value.equalsIgnoreCase(value)) return p;
        }
        throw new IllegalArgumentException("Unknown geocoder provider: " + value);
    }
}

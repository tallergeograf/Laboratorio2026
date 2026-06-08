package uy.edu.taller.sige.geo_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SpecificAddressResultId implements Serializable {

    @Column(name = "direccion_id")
    private Long direccionId;

    @Column(name = "geocoder_id")
    private String geocoderId;

    public SpecificAddressResultId() {}

    public SpecificAddressResultId(Long  direccionId, String geocoderId) {
        this.direccionId = direccionId;
        this.geocoderId = geocoderId;
    }

    public Long  getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(Long  direccionId) {
        this.direccionId = direccionId;
    }

    public String getGeocoderId() {
        return geocoderId;
    }

    public void setGeocoderId(String geocoderId) {
        this.geocoderId = geocoderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecificAddressResultId)) return false;
        SpecificAddressResultId that = (SpecificAddressResultId) o;
        return Objects.equals(direccionId, that.direccionId)
                && Objects.equals(geocoderId, that.geocoderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(direccionId, geocoderId);
    }
}

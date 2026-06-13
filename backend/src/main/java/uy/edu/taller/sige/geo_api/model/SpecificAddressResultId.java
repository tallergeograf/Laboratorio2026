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

    @Column(name = "is_demo")
    private Boolean isDemo;

    public SpecificAddressResultId() {}

    public SpecificAddressResultId(Long direccionId, String geocoderId, boolean isDemo) {
        this.direccionId = direccionId;
        this.geocoderId = geocoderId;
        this.isDemo = isDemo;
    }

    public Long getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(Long direccionId) {
        this.direccionId = direccionId;
    }

    public String getGeocoderId() {
        return geocoderId;
    }

    public void setGeocoderId(String geocoderId) {
        this.geocoderId = geocoderId;
    }

    public Boolean getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(Boolean isDemo) {
        this.isDemo = isDemo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecificAddressResultId)) return false;
        SpecificAddressResultId that = (SpecificAddressResultId) o;
        return Objects.equals(direccionId, that.direccionId)
                && Objects.equals(geocoderId, that.geocoderId)
                && Objects.equals(isDemo, that.isDemo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(direccionId, geocoderId, isDemo);
    }
}

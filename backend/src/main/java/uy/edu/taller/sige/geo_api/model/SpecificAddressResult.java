package uy.edu.taller.sige.geo_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "direcciones_resultados")
public class SpecificAddressResult {

    @EmbeddedId
    private SpecificAddressResultId id;

    @ManyToOne
    @MapsId("direccionId")
    @JoinColumn(name = "direccion_id")
    private SpecificAddress direccion;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "is_result")
    private Boolean isResult;

    @Column(name = "latencia")
    private Double latencia;

    @Column(name = "status_code")
    private Integer statusCode;

    public SpecificAddressResult() {}

    public SpecificAddressResultId getId() {
        return id;
    }

    public void setId(SpecificAddressResultId id) {
        this.id = id;
    }

    public SpecificAddress getDireccion() {
        return direccion;
    }

    public void setDireccion(SpecificAddress direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Boolean getIsResult() {
        return isResult;
    }

    public void setIsResult(Boolean isResult) {
        this.isResult = isResult;
    }

    public Double getLatencia() {
        return latencia;
    }

    public void setLatencia(Double latencia) {
        this.latencia = latencia;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}

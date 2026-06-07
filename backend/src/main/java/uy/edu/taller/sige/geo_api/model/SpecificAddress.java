package uy.edu.taller.sige.geo_api.model;

import jakarta.persistence.*;
import uy.edu.taller.sige.geo_api.model.enums.AddressCategory;
import uy.edu.taller.sige.geo_api.model.enums.AddressType;

@Entity
@Table(name = "direcciones")
public class SpecificAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "direccionCompleta")
    private String direccionCompleta;

    @Column(name = "departamento")
    private String departamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private AddressCategory categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipodireccion")
    private AddressType tipoDireccion;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    public SpecificAddress() {}

    // getters y setters

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public Long getId() {
        return id;
    }

    public String getFullAddress() {
        return direccionCompleta;
    }

    public void setFullAddress(String fullAddress) {
        this.direccionCompleta = fullAddress;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public AddressCategory getCategoria() {
        return categoria;
    }

    public void setCategoria(AddressCategory categoria) {
        this.categoria = categoria;
    }

    public AddressType getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(AddressType tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDireccionCompleta(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
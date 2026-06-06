package uy.edu.taller.sige.geo_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "vista_uruguay")
public class Address {

    @Id
    @Column(name = "punto_wkb")
    private String puntoWkb;

    /*@Column(name = "geom", columnDefinition = "geometry(Point,4326)")
    @Transient
    private Point geom;*/

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "nombre_via")
    private String nombreVia;

    @Column(name = "num_puerta")
    private String numPuerta;

    @Column(name = "km")
    private String km;

    @Column(name = "manzana")
    private String manzana;

    @Column(name = "solar")
    private String solar;

    @Column(name = "nombre_inmueble")
    private String nombreInmueble;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "departamento")
    private String departamento;

    public Address() {
    }

    /*public Point getGeom() {
        return geom;
    }*/

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public String getPuntoWkb() {
        return puntoWkb;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public String getNumPuerta() {
        return numPuerta;
    }


    public String getKm() {
        return km;
    }

    public String getManzana() {
        return manzana;
    }

    public String getSolar() {
        return solar;
    }

    public String getNombreInmueble() {
        return nombreInmueble;
    }

    public String getLocalidad() {
        return localidad;
    }


    public String getDepartamento() {
        return departamento;
    }


    /*public void setGeom(Point geom) {
        this.geom = geom;
    }*/

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public void setPuntoWkb(String puntoWkb) {
        this.puntoWkb = puntoWkb;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public void setNumPuerta(String numPuerta) {
        this.numPuerta = numPuerta;
    }


    public void setKm(String km) {
        this.km = km;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public void setSolar(String solar) {
        this.solar = solar;
    }

    public void setNombreInmueble(String nombreInmueble) {
        this.nombreInmueble = nombreInmueble;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

}
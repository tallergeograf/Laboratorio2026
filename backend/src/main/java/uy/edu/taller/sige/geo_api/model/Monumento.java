package uy.edu.taller.sige.geo_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "direcciones_monumentos")
public class Monumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "osm_id", unique = true, nullable = false)
    private Long osmId;

    @Column(name = "nombre")
    private String name;

    @Column(name = "latitud", nullable = false)
    private Double lat;

    @Column(name = "longitud", nullable = false)
    private Double lon;

    @Column(name = "nombre_via")
    private String street;

    @Column(name = "localidad")
    private String city;

    public Monumento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOsmId() { return osmId; }
    public void setOsmId(Long osmId) { this.osmId = osmId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}

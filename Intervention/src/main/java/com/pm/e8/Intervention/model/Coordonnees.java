package com.pm.e8.Intervention.model;

import javax.persistence.*;

@Entity
public class Coordonnees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double lon;
    private double lat;
    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "intervention_id", nullable = false)
    private Intervention intervention;

    public Coordonnees(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;

    }

    public Coordonnees() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    private Intervention getIntervention() {
        return intervention;
    }

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    @Override
    public String toString() {
        return "lon: " + lon + "-lat: " + lat;
    }
}

package com.pm.e8.FireManager.model;

import com.project.model.dto.Coord;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Fire {
    @Id
    private int id;
    private String type;
    private float intensity;
    private float range;
    private double lon;
    private double lat;

    public Fire() {
    }

    public Fire(Integer id, String type, float intensity, float range, double lon, double lat) {
        super();
        this.id = id;
        this.type = type;
        this.intensity = intensity;
        this.range = range;
        this.lon = lon;
        this.lat = lat;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public float getIntensity() {
        return intensity;
    }
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
    public float getRange() {
        return range;
    }
    public void setRange(float range) {
        this.range = range;
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

    public Coord getCoord() {
        return new Coord(this.lon, this.lat);
    }

    @Override
    public String toString() {
       return "Fire [id=" + id + ", type=" + type + ", intensity=" + intensity + ", range=" + range + ", lon=" + lon
                + ", lat=" + lat + "]";
    }

}

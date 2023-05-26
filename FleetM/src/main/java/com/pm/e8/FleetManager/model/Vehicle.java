package com.pm.e8.FleetManager.model;

import com.project.model.dto.LiquidType;
import com.project.model.dto.VehicleDto;
import com.project.model.dto.VehicleType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vehicle {
    @Id
    private Integer id;
    private double lon;
    private double lat;
    private VehicleType type;
    private LiquidType liquidType;
    private float liquidQuantity;
    private float fuel;
    private int crewMember;
    private Integer facilityRefID;
    @Column(nullable = true)
    private double futureLon;
    @Column(nullable = true)
    private double futureLat;

    public Vehicle() {
    }

    public Vehicle(VehicleDto vehicleDto){
        this.id = vehicleDto.getId();
        this.lon = vehicleDto.getLon();
        this.lat = vehicleDto.getLat();
        this.type = vehicleDto.getType();
        this.liquidType = vehicleDto.getLiquidType();
        this.liquidQuantity = vehicleDto.getLiquidQuantity();
        this.fuel = vehicleDto.getFuel();
        this.crewMember = vehicleDto.getCrewMember();
        this.facilityRefID = vehicleDto.getFacilityRefID();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public LiquidType getLiquidType() {
        return liquidType;
    }

    public void setLiquidType(LiquidType liquidType) {
        this.liquidType = liquidType;
    }

    public float getLiquidQuantity() {
        return liquidQuantity;
    }

    public void setLiquidQuantity(float liquidQuantity) {
        this.liquidQuantity = liquidQuantity;
    }

    public float getFuel() {
        return fuel;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public int getCrewMember() {
        return crewMember;
    }

    public void setCrewMember(int crewMember) {
        this.crewMember = crewMember;
    }

    public Integer getFacilityRefID() {
        return facilityRefID;
    }

    public void setFacilityRefID(Integer facilityRefID) {
        this.facilityRefID = facilityRefID;
    }

    public double getFutureLon() {
        return futureLon;
    }

    public void setFutureLon(double futureLon) {
        this.futureLon = futureLon;
    }

    public double getFutureLat() {
        return futureLat;
    }

    public void setFutureLat(double futureLat) {
        this.futureLat = futureLat;
    }
}

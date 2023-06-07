package com.pm.e8.FleetManager.model;

import com.project.model.dto.LiquidType;
import com.project.model.dto.VehicleDto;
import com.project.model.dto.VehicleType;
import java.util.List;
import javax.persistence.*;

@Entity
public class Vehicle {
    @Id
    @Column(name = "vehicle_id")
    private Integer id;
    private double lon;
    private double lat;
    private VehicleType type;
    private LiquidType liquidType;
    private float liquidQuantity;
    private float fuel;
    private int crewMember;
    private boolean inMovement;
    private Integer facilityRefID;
    public Double futureLon;
    public Double futureLat;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "vehicle")
    private List<Coordonnees> coordonneesList;


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
        this.inMovement = false; //regarder si c'est pas pcq on l'initialise comme ca qu'il est toujours false
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

    public Double getFutureLon() {
        return futureLon;
    }

    public void setFutureLon(Double futureLon) {
        this.futureLon = futureLon;
    }

    public double getFutureLat() {
        return futureLat;
    }

    public void setFutureLat(Double futureLat) {
        this.futureLat = futureLat;
    }

    public List<Coordonnees> getCoordonnees() {
        return coordonneesList;
    }

    public void setCoordonnees(List<Coordonnees> coordonneesList) {
        this.coordonneesList = coordonneesList;
    }

    public List<Coordonnees> getCoordonneesList() {
        return coordonneesList;
    }

    public void setInMovement(boolean inMovement) {
        this.inMovement = inMovement;
    }

    /*public boolean getIsInMovement() {
        return this.inMovement;
    }*/

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", lon=" + lon +
                ", lat=" + lat +
                ", type=" + type +
                ", liquidType=" + liquidType +
                ", liquidQuantity=" + liquidQuantity +
                ", fuel=" + fuel +
                ", crewMember=" + crewMember +
                ", facilityRefID=" + facilityRefID +
                ", futureLon=" + futureLon +
                ", futureLat=" + futureLat +
                '}';
    }

    public boolean isInMovement() {
        return this.inMovement;
    }
}

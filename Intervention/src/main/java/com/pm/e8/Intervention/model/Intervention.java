package com.pm.e8.Intervention.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Intervention {
    @Id
    @GeneratedValue
    private int id;
    private int idFire;
    private int idVehicle;
    private String status = "En cours";


    public Intervention() {

    }

    public Intervention(int fireId, int vehicleId) {
        this.idFire = fireId;
        this.idVehicle = vehicleId;
    }

    protected int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public int getIdFire() {
        return idFire;
    }

    protected void setIdFire(int idFire) {
        this.idFire = idFire;
    }

    protected int getIdVehicle() {
        return idVehicle;
    }

    protected void setIdVehicle(int idVehicle) {
        this.idVehicle = idVehicle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

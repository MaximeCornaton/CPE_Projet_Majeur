package com.pm.e8.Intervention.model;

import com.project.model.dto.Coord;

import javax.persistence.*;
import java.util.List;

@Entity
public class Intervention {
    @Id
    @GeneratedValue
    private int id;
    private int idFire;
    private int idVehicle;
    private Status status = Status.EN_COURS;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "intervention")
    private List<Coordonnees> coordonneesList;

    public Intervention() {

    }

    public Intervention(int fireId, int vehicleId) {
        this.idFire = fireId;
        this.idVehicle = vehicleId;
    }

    public List<Coordonnees> getCoordonnees() {
        return coordonneesList;
    }
    public void setCoordonnees(List<Coord> coordonnees) {
        this.coordonneesList = coordonnees;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

package com.pm.e8.Intervention.service;


import com.pm.e8.Intervention.model.Coordonnees;
import com.pm.e8.Intervention.model.Intervention;
import com.pm.e8.Intervention.model.Status;
import com.pm.e8.Intervention.repository.InterventionRepository;
import com.pm.e8.Intervention.tools.PolylineSplitter;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import com.project.model.dto.LiquidType;
import com.project.model.dto.VehicleDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterventionService {

    private final VehicleRestClientService vehicleRestClientService;
    private final FireRestClientService fireRestClientService;
    private final InterventionRepository iRepo;
    private final MapRestClientService mapRestClientService;

    public InterventionService(VehicleRestClientService vehicleRestClientService, FireRestClientService fireRestClientService, InterventionRepository iRepo, MapRestClientService mapRestClientService) {
        this.vehicleRestClientService = vehicleRestClientService;
        this.fireRestClientService = fireRestClientService;
        this.iRepo = iRepo;
        this.mapRestClientService = mapRestClientService;
    }

    public void AutoIntervention(int fireId, int vehicleId) {
        System.out.println();
        LiquidType liquidType = LiquidType.ALL;

        try {
            FireDto fire = fireRestClientService.getFire(fireId);
            System.out.println(fire.getId() + " " + fire.getType());
            liquidType = getMostEfficientLiquid(fire.getType());
            liquidType = getMostEfficientLiquid(fire.getType());
            System.out.println(liquidType);
        } catch (Exception e) {
            System.out.println("No liquid type found");
        }

        Intervention I = new Intervention(fireId, vehicleId);
        for (Intervention i : iRepo.findAll()) {
            if ((i.getIdFire() == fireId || fireId == -1)) {
                return;
            }
        }

        I.setCoordonnees(getTrajetList(vehicleId, fireId));
        System.out.println(iRepo.save(I));

        vehicleRestClientService.updateVehicleLiquidType(vehicleId, liquidType);
    }

    public void createIntervention(int fireId, int vehicleId) {
        FireDto fire = fireRestClientService.getFire(fireId);
        LiquidType liquidType = getMostEfficientLiquid(fire.getType());

        for(Intervention i : iRepo.findAll()){
            if(i.getIdFire() == fireId || fireId == -1){
                return;
            }
        }

        Intervention I = new Intervention(fireId, vehicleId);
        I.setCoordonnees(getTrajetList(vehicleId, fireId));

        System.out.println(iRepo.save(I));
        System.out.println(iRepo.findAll());

        vehicleRestClientService.updateVehicleLiquidType(vehicleId, liquidType);
        vehicleRestClientService.createIntervention(vehicleId, new Coord(fire.getLon(),fire.getLat()));
    }

    private LiquidType getMostEfficientLiquid(String type) {
        LiquidType mostEfficient = LiquidType.ALL;
        float maxEfficiency = -1;
        for (LiquidType liquidType : LiquidType.values()) {
            float efficiency = liquidType.getEfficiency(type);
            if (efficiency > maxEfficiency) {
                maxEfficiency = efficiency;
                mostEfficient = liquidType;
            }
        }
        return mostEfficient;
    }

    public Iterable<Intervention> getInterventions(){
        return iRepo.findAll();
    }

    public void cleanInter(){
        Iterable<Intervention> InterventionList = getInterventions();
        for(Intervention i : InterventionList){
            if(i.getStatus().equals(Status.TERMINEE)){
                iRepo.delete(i);
            }
        }
    }

    public List<Intervention> doneIntervention() {
        return iRepo.findAllByStatus(Status.TERMINEE);
    }

    public List<Intervention> inProgressIntervention() {
        return iRepo.findAllByStatus(Status.EN_COURS);
    }

    private List<Coordonnees> getTrajetList(int vehicleId, int fireId) {
        FireDto fire = fireRestClientService.getFire(fireId);
        List<Coordonnees> coordonneesList;
        VehicleDto vehicle = vehicleRestClientService.getVehicle(vehicleId);
        try {
            Coord coord = new Coord(fire.getLon(), fire.getLat());
            String polyline = mapRestClientService.getPolyline(new Coord(vehicle.getLon(), vehicle.getLat()), coord);
            List<Coord> coordList = PolylineSplitter.cutPolyline(polyline, vehicle.getType().getMaxSpeed() / 1000);
            coordonneesList = new ArrayList<>();

            for (Coord c : coordList) {
                coordonneesList.add(new Coordonnees(c.getLon(), c.getLat()));
            }
        } catch (Exception e) {
            coordonneesList = new ArrayList<>();
        }
        return coordonneesList;
    }



    /*
    public void createInterventions(List<Integer> fireIds, List<Integer> vehicleIds) {
        int tailleMin = Math.min(fireIds.size(), vehicleIds.size());
        for (int i=0; i<tailleMin; i++) {
            int vehicleId = vehicleIds.get(i);
            int fireId = fireIds.get(i);
            createIntervention(fireId, vehicleId);
        }
    }*/
}

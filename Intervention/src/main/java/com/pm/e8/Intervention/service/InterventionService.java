package com.pm.e8.Intervention.service;

import com.google.common.collect.Lists;
import com.pm.e8.Intervention.model.Intervention;
import com.pm.e8.Intervention.model.Status;
import com.pm.e8.Intervention.repository.InterventionRepository;
import com.pm.e8.Intervention.tools.PolylineSplitter;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import com.project.model.dto.LiquidType;
import com.project.model.dto.VehicleDto;
import org.springframework.stereotype.Service;

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

    public void createIntervention(int fireId, int vehicleId) {
        //TODO Verfiier que on peut automatiser le mouvement en creant des interventions
        FireDto fire = fireRestClientService.getFire(fireId);
        LiquidType liquidType = getMostEfficientLiquid(fire.getType());
        VehicleDto vehicle = vehicleRestClientService.getVehicle(vehicleId);

        Coord coord = new Coord(fire.getLon(),fire.getLat());
        String polyline = mapRestClientService.getPolyline(new Coord(vehicle.getLon(),vehicle.getLat()),coord);
        List<Coord> coordList = PolylineSplitter.cutPolyline(polyline, vehicle.getType().getMaxSpeed()/1000);

        for(Intervention i : iRepo.findAll()){
            if(i.getIdFire() == fireId){
                return;
            }
        }

        Intervention I = new Intervention(fireId, vehicleId);
        I.setCoordonnees(coordList);
        iRepo.save(I);

        vehicleRestClientService.updateVehicleLiquidType(vehicleId, liquidType);
        vehicleRestClientService.createIntervention(vehicleId, new Coord(fire.getLon(),fire.getLat()));
    }

    private LiquidType getMostEfficientLiquid(String type) {
        LiquidType mostEfficient = null;
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

    public List<Intervention> getInterventions(){
        return Lists.newArrayList(iRepo.findAll());
    }

    public void cleanInter(){
        List<Intervention> InterventionList = getInterventions();
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

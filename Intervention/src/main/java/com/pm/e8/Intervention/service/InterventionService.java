package com.pm.e8.Intervention.service;

import com.google.common.collect.Lists;
import com.pm.e8.Intervention.model.Intervention;
import com.pm.e8.Intervention.repository.InterventionRepository;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import com.project.model.dto.FireType;
import com.project.model.dto.LiquidType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterventionService {

    private final VehicleRestClientService vehicleRestClientService;
    private final FireRestClientService fireRestClientService;
    private final InterventionRepository iRepo;

    public InterventionService(VehicleRestClientService vehicleRestClientService, FireRestClientService fireRestClientService, InterventionRepository iRepo) {
        this.vehicleRestClientService = vehicleRestClientService;
        this.fireRestClientService = fireRestClientService;
        this.iRepo = iRepo;
    }

    public void createIntervention(int fireId, int vehicleId) {
        FireDto fire = fireRestClientService.getFire(fireId);
        LiquidType liquidType = getMostEfficientLiquid(fire.getType());
        Intervention I = new Intervention(fireId, vehicleId);
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
            if(i.getStatus().equals("Terminé")){
                iRepo.delete(i);
            }
        }

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

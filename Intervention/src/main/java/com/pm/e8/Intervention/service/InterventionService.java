package com.pm.e8.Intervention.service;

import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import com.project.model.dto.FireType;
import org.springframework.stereotype.Service;

@Service
public class InterventionService {

    private final VehicleRestClientService vehicleRestClientService;
    private final FireRestClientService fireRestClientService;

    public InterventionService(VehicleRestClientService vehicleRestClientService, FireRestClientService fireRestClientService) {
        this.vehicleRestClientService = vehicleRestClientService;
        this.fireRestClientService = fireRestClientService;
    }

    public void createIntervention(int fireId, int vehicleId) {
        FireDto fire = fireRestClientService.getFire(fireId);
        vehicleRestClientService.createIntervention(vehicleId, new Coord(fire.getLon(),fire.getLat()));
    }
}

package com.pm.e8.Intervention.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pm.e8.Intervention.service.InterventionService;

import java.util.List;

@RestController
public class InterventionCrt {

    private final InterventionService interventionService;

    public InterventionCrt(InterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @PostMapping("/intervention")
    public void createIntervention(@RequestParam int fireId, int vehicleId){
        interventionService.createIntervention(fireId,vehicleId);
    }

/*
    @PostMapping("/interventions")
    public void createInterventions(@RequestParam List<Integer> fireIds, List<Integer> vehicleIds){
        for (int vehicleId : vehicleIds) {
            interventionService.createInterventions(fireIds,vehicleIds);
        }
    }*/
}

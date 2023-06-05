package com.pm.e8.Intervention.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pm.e8.Intervention.model.Intervention;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/interventions")
    public List<Intervention> getInterventions(){
        return interventionService.getInterventions();
    }

    @DeleteMapping("/intervention/clean")
    public void cleanInter(){
        interventionService.cleanInter();
    }

/*
    @PostMapping("/interventions")
    public void createInterventions(@RequestParam List<Integer> fireIds, List<Integer> vehicleIds){
        for (int vehicleId : vehicleIds) {
            interventionService.createInterventions(fireIds,vehicleIds);
        }
    }*/
}

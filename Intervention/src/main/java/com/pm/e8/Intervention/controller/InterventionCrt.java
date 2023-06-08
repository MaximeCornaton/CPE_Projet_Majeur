package com.pm.e8.Intervention.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pm.e8.Intervention.model.Coordonnees;
import com.pm.e8.Intervention.model.Intervention;
import com.project.model.dto.Coord;
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
    public Iterable<Intervention> getInterventions(){
        return interventionService.getInterventions();
    }

    @DeleteMapping("/interventions/clean")
    public void cleanInter(){
        interventionService.cleanInter();
    }

    @GetMapping("/interventions/done")
    public List<Intervention> doneIntervention(){
        return interventionService.doneIntervention();
    }

    @GetMapping("/interventions/inProgress")
    public List<Intervention> inProgressIntervention(){
        return interventionService.inProgressIntervention();
    }

    @PostMapping("/intervention/autoIntervention/{fireId}/{vehicleId}")
    public void AutoIntervention(@PathVariable int fireId, @PathVariable int vehicleId){
        interventionService.AutoIntervention(fireId,vehicleId);
    }

/*
    @PostMapping("/interventions")
    public void createInterventions(@RequestParam List<Integer> fireIds, List<Integer> vehicleIds){
        for (int vehicleId : vehicleIds) {
            interventionService.createInterventions(fireIds,vehicleIds);
        }
    }*/
}

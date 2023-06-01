package com.pm.e8.FacilityManager.controller;

import com.pm.e8.FacilityManager.model.Facility;
import com.pm.e8.FacilityManager.service.FacilityService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableScheduling
public class FacilityCrt {

    private FacilityService fServ;

    public FacilityCrt(FacilityService fServ) {
        this.fServ = fServ;
    }

    @GetMapping("/facility/team")
    public ArrayList<Facility> getFacilityTeam() {
        return fServ.getFacilityTeam();
    }

    @GetMapping("/facility")
    public List<Facility> getFacilityList() {
        return fServ.getFacilityList();
    }


}

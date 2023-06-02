package com.pm.e8.FacilityManager.service;

import com.google.common.collect.Lists;
import com.pm.e8.FacilityManager.model.Facility;
import com.pm.e8.FacilityManager.repository.FacilityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacilityService {

    private FacilityRepository fRepo;

    public FacilityService(FacilityRepository fRepo) {
        this.fRepo = fRepo;
    }

    public ArrayList<Facility> getFacilityTeam() {

        ArrayList<Facility> FacilityList = Lists.newArrayList((Facility) fRepo.findByName("Caserne Tout feu tout flamme"));
        FacilityList.add((Facility) fRepo.findByName("Caserne 2 Tout feu tout flamme"));
        return FacilityList;
    }

    public List<Facility> getFacilityList() {
        return Lists.newArrayList(fRepo.findAll());
    }
}

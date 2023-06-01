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
        return Lists.newArrayList((Facility) fRepo.findByTeamUuid("4c848170-4287-4c5f-9e1b-3d16ae2d4470"));

    }

    public List<Facility> getFacilityList() {
        return Lists.newArrayList(fRepo.findAll());
    }
}

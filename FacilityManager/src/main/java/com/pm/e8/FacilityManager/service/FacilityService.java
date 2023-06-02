package com.pm.e8.FacilityManager.service;

import com.google.common.collect.Lists;
import com.pm.e8.FacilityManager.model.Facility;
import com.pm.e8.FacilityManager.repository.FacilityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FacilityService {

    private FacilityRepository fRepo;

    public FacilityService(FacilityRepository fRepo) {
        this.fRepo = fRepo;
    }

    public ArrayList<Facility> getFacilityTeam() {
        ArrayList<Facility> FacilityList = new ArrayList<>();
        Optional<Facility> f1 = fRepo.findById(38);
        Optional<Facility> f2 = fRepo.findById(3935);

        if (f1.isPresent()) {
            FacilityList.add(f1.get());
        }
        if (f2.isPresent()) {
            FacilityList.add(f2.get());
        }

        return FacilityList;
    }

    public List<Facility> getFacilityList() {
        return Lists.newArrayList(fRepo.findAll());
    }
}

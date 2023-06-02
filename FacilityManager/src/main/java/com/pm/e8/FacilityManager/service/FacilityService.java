package com.pm.e8.FacilityManager.service;

import com.google.common.collect.Lists;
import com.pm.e8.FacilityManager.model.Facility;
import com.pm.e8.FacilityManager.repository.FacilityRepository;
import com.project.model.dto.FireDto;
import com.project.model.dto.VehicleDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.lang.Math;

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

    public void automatique(){
        List<FireDto> FireList = getFireList();
        List<VehicleDto> VehicleList = getVehicleList();





    }

    private List<VehicleDto> getVehicleList() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "127.0.0.1:8000/fire-service/fires";
        ResponseEntity<List<VehicleDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VehicleDto>>() {}
        );
        List<VehicleDto> VehicleDtoList = response.getBody();

        try {
            if (VehicleDtoList == null) {
                throw new Exception("No camion found");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        assert VehicleDtoList != null;
        return VehicleDtoList;
    }

    private List<FireDto> getFireList(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "127.0.0.1:8000/fire-service/fires";
        ResponseEntity<List<FireDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FireDto>>() {}
        );
        List<FireDto> fireList = response.getBody();

        try {
            if (fireList == null) {
                throw new Exception("No fires found");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        assert fireList != null;
        return fireList;
    }
}

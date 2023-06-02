package com.pm.e8.FacilityManager.service;

import com.google.common.collect.Lists;
import com.pm.e8.FacilityManager.model.Facility;
import com.pm.e8.FacilityManager.repository.FacilityRepository;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import com.project.model.dto.VehicleDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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


    private List<VehicleDto> getPumperList() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "127.0.0.1:8000/fleet-service/vehicles/pumper";
        ResponseEntity<List<VehicleDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VehicleDto>>() {}
        );
        List<VehicleDto> PumperDtoList = response.getBody();

        try {
            if (PumperDtoList == null) {
                throw new Exception("No camion found");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        assert PumperDtoList != null;
        return PumperDtoList;
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

    private boolean getfireById(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1/fire-service/fires/"+id;
        ResponseEntity<FireDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<FireDto>() {}
        );
        FireDto fire = response.getBody();
        if (fire != null) {
            return true;
        }
        return false;
    }

    private void launchInter(Integer idFire, Integer idVehicle) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8000/inter-service/intervention?fireId"+idFire+"&vehicleId="+idVehicle;
        restTemplate.postForObject(url, null, String.class);

    }

    public Double getDistanceBetweenCoords(Coord coord1, Coord coord2){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/fire/distance";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("latCoord1", coord1.getLat())
                .queryParam("lonCoord1", coord1.getLon())
                .queryParam("latCoord2", coord2.getLat())
                .queryParam("lonCoord2", coord2.getLon());
        return restTemplate.getForObject(builder.toUriString(), Double.class);
    }
}

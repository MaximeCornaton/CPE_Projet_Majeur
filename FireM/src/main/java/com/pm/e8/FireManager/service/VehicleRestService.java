package com.pm.e8.FireManager.service;

import com.project.model.dto.VehicleDto;
import org.springframework.web.client.RestTemplate;

public class VehicleRestService {

    public VehicleRestService() {
    }

    public VehicleDto getVehicle(int idv) {
        RestTemplate restTemplate = new RestTemplate();
        VehicleDto v = restTemplate.getForObject("http://localhost:8000/fleet-service/vehicle/" + idv, VehicleDto.class);
        assert v != null;
        return v;
    }
}

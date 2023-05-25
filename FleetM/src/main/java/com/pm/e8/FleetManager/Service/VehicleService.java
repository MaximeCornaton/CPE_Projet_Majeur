package com.pm.e8.FleetManager.Service;

import com.project.model.dto.VehicleDto;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class VehicleService {

    private List VehicleDto = new ArrayList<VehicleDto>();

    public void setVehicleList() {
        RestTemplate restTemplate = new RestTemplate();
        String Url = "http://vps.cpe-sn.fr:8081/vehicle";
        fireDtoList  = restTemplate.getForObject(Url, List.class);
    }


    public float getFuelLevel(){
        return vehicleDto.getFuel();
    }

    public int getVehicle(){
        return vehicleDto.getId();
    }

    public VehicleDto moveVehicle(int id, int x, int y) {
            //TODO
        return null;
    }
}

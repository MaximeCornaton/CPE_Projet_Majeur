package com.pm.e8.Intervention.service;

import com.project.model.dto.Coord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VehicleRestClientService {

        public void createIntervention(int vehicleId, Coord coord) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/vehicle/move/{id}";
            restTemplate.put(url, coord, vehicleId);
        }
}

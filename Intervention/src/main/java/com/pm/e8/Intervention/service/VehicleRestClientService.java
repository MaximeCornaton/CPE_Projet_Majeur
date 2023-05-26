package com.pm.e8.Intervention.service;

import com.project.model.dto.Coord;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VehicleRestClientService {

        public void createIntervention(int vehicleId, Coord coord) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/vehicle/move/{id}";
            restTemplate.put(url, coord, vehicleId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Coord> request = new HttpEntity<>(coord, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class, vehicleId);
            if(response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Vehicle moved successfully");
            } else {
                System.out.println("Failed to move vehicle");
            }
        }
}

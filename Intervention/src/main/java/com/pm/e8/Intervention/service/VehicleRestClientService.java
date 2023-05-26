package com.pm.e8.Intervention.service;

import com.project.model.dto.Coord;
import com.project.model.dto.LiquidType;
import com.project.model.dto.VehicleDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class VehicleRestClientService {

    @Value(value = "${team.uuid}")
    private String uuid;

        public void createIntervention(int vehicleId, Coord coord) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/vehicle/move/{id}";
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

    public void updateVehicleLiquidType(int vehicleId, LiquidType liquidType) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/vehicle/{id}/liquidType";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("liquidType", liquidType);
        restTemplate.put(builder.buildAndExpand(vehicleId).toUri(), null);
    }
}

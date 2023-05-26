package com.pm.e8.Intervention.service;

import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FireRestClientService {

    public Coord getFireCoord(int fireId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/fireCoord/{id}";
        ResponseEntity<Coord> response = restTemplate.getForEntity(url, Coord.class, fireId);
        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public FireDto getFire(int fireId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/fires/{id}";
        ResponseEntity<FireDto> response = restTemplate.getForEntity(url, FireDto.class, fireId);
        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }
}

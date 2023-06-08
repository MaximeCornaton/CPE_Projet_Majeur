package com.pm.e8.Intervention.service;

import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    public FireDto getFire(int id) {
        RestTemplate restTemplate = new RestTemplate();
        String Url = "http://vps.cpe-sn.fr:8081/fire/{id}";
        FireDto fireDto = restTemplate.getForObject(Url, FireDto.class, id);
        try {
            if (fireDto == null) {
                throw new Exception("No fire found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fireDto;
    }

    public List<FireDto> getFires(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8000/fire-service/fires";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return null;
        }
    }
}

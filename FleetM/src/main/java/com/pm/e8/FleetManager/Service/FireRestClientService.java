package com.pm.e8.FleetManager.Service;

import com.project.model.dto.FireDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;


import java.util.List;
import java.util.Objects;

@Service
public class FireRestClientService {

    public List<FireDto> getAllFires(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/fires";
        ResponseEntity<FireDto[]> responseEntity = restTemplate.getForEntity(url, FireDto[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    public FireDto getFireDtoById(int id) {
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
}

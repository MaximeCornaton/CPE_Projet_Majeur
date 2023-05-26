package com.pm.e8.FleetManager.Service;

import com.project.model.dto.FireDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FireRestClientService {

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

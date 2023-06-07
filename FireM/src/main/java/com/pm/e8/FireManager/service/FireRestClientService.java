package com.pm.e8.FireManager.service;

import com.pm.e8.FireManager.model.Fire;
import com.project.model.dto.FireDto;
import com.project.model.dto.VehicleDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FireRestClientService {

    public List<FireDto> getAllFires() {
        RestTemplate restTemplate = new RestTemplate();
        String Url = "http://vps.cpe-sn.fr:8081/fires";
        FireDto[] fireDtoList = restTemplate.getForObject(Url, FireDto[].class);
        try {
            if(fireDtoList == null) {
                throw new Exception("No fires found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return Arrays.asList(fireDtoList);
    }

    public static FireDto getFireDto(int id) {
        RestTemplate restTemplate = new RestTemplate();
        String Url = "http://vps.cpe-sn.fr:8081/fire/{id}";
        FireDto fireDto = restTemplate.getForObject(Url, FireDto.class, id);
        try {
            if(fireDto == null) {
                throw new Exception("No fire found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fireDto;
    }

    public Fire getFire(int idf) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/fire/{id}";
        return restTemplate.getForObject(url, Fire.class, idf);
    }
}

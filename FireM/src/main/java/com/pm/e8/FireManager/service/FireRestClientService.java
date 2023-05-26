package com.pm.e8.FireManager.service;

import com.project.model.dto.FireDto;
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

    public FireDto getNearestFire(FireDto fire) {
        //TODO : get the nearest fire from the list
        setFireList();
        FireDto NearestFire = new FireDto();

        return NearestFire;
    }
}

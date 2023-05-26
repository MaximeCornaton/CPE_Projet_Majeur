package com.pm.e8.FireManager.service;

import com.project.model.dto.FireDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;


import java.util.List;

@Service
public class FireService {
    private List fireDtoList = new ArrayList<FireDto>();
    private final FireRestClientService fireRestClientService;

    public FireService(FireRestClientService fireRestClientService) {
        this.fireRestClientService = fireRestClientService;
    }

    public void setFireList() {
        RestTemplate restTemplate = new RestTemplate();
        String Url = "http://vps.cpe-sn.fr:8081/fires";
        fireDtoList  = restTemplate.getForObject(Url, List.class);
    }

    public List<FireDto> getFireDtoList() {
        return fireRestClientService.getAllFires();
    }

    public FireDto getNearestFire(FireDto fire) {
        //TODO : get the nearest fire from the list
        setFireList();
        FireDto NearestFire = new FireDto();

        return NearestFire;
    }
}

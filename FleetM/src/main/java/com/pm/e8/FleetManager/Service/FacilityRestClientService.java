package com.pm.e8.FleetManager.Service;

import com.project.model.dto.FacilityDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacilityRestClientService {

    public FacilityDto getFacility(int id){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/facility/{id}";
        return restTemplate.getForObject(url, FacilityDto.class, id);
    }
}

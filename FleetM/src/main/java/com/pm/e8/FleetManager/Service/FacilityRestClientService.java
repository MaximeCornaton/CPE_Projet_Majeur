package com.pm.e8.FleetManager.Service;

import com.project.model.dto.FacilityDto;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacilityRestClientService {

    public FacilityDto getFacilityDtoById(int id) {
        RestTemplate restTemplate = new RestTemplate();
        String Url = "http://vps.cpe-sn.fr:8081/facility/{id}";
        FacilityDto facilityDto = restTemplate.getForObject(Url, FacilityDto.class, id);
        try {
            if (facilityDto == null) {
                throw new Exception("No facility found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return facilityDto;
    }
    public FacilityDto getFacility(int id){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/facility/{id}";
        return restTemplate.getForObject(url, FacilityDto.class, id);
    }
}

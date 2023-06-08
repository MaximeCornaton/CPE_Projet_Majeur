package com.pm.e8.FleetManager.Service;

import com.project.model.dto.Coord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.List;

@Service
public class InterventionRestClientService {
    public void AutoInter(int vehicleId, int fireId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8000/inter-service/intervention/autoIntervention/"+fireId+"/"+vehicleId;
        //String uri = "http://localhost:8000/inter-service/intervention/autoIntervention/{fireId}/{vehicleId}"
        restTemplate.postForObject(url, null, String.class);
    }
}

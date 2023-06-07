package com.pm.e8.FleetManager.Service;

import com.project.model.dto.Coord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.List;

@Service
public class InterventionRestClientService {


    public void AutoInter(int vehicleId, int fireId, List<Coord> coordList) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8000/inter-service/intervention/autoIntervention/"+fireId+"/"+vehicleId;

        Coord[] coordArray = coordList.toArray(new Coord[coordList.size()]);
        restTemplate.postForObject(url,coordArray,Coord[].class);
    }
}

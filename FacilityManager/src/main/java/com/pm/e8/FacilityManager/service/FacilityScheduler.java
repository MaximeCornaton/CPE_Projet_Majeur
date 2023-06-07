package com.pm.e8.FacilityManager.service;

import com.pm.e8.FacilityManager.model.Facility;
import com.pm.e8.FacilityManager.repository.FacilityRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FacilityScheduler {

    private final FacilityRepository fRepo;

    public FacilityScheduler(FacilityRepository fRepo) {
        this.fRepo = fRepo;
    }

    @Scheduled(fixedRate = 5000)
    public void UpdateBDD() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/facility";
        ResponseEntity<List<Facility>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Facility>>() {}
        );
        List<Facility> facilityList = response.getBody();

        try {
            if (facilityList == null) {
                throw new Exception("No facility found");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        assert facilityList != null;
        fRepo.saveAll(facilityList);
    }

}

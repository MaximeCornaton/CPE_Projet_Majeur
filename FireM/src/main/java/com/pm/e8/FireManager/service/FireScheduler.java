package com.pm.e8.FireManager.service;

import com.pm.e8.FireManager.model.Fire;
import com.pm.e8.FireManager.repository.FireRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
@Component
public class FireScheduler {
    private final FireRepository fRepo;

    public FireScheduler(FireRepository fRepo) {
        this.fRepo = fRepo;
    }

    @Scheduled(fixedRate = 2000)
    public void UpdateBDD() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/fires";
        ResponseEntity<List<Fire>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Fire>>() {}
        );
        List<Fire> fireList = response.getBody();

        try {
            if (fireList == null) {
                throw new Exception("No fires found");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        assert fireList != null;
        fRepo.saveAll(fireList);
        }
    }

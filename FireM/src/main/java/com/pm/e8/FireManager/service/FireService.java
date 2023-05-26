package com.pm.e8.FireManager.service;

import com.pm.e8.FireManager.model.Fire;
import com.pm.e8.FireManager.repository.FireRepository;
import com.project.model.dto.FireDto;
import org.springframework.stereotype.Service;
import com.pm.e8.FireManager.model.Fire;

import java.util.List;
import java.util.Optional;

@Service
public class FireService {
    private final FireRestClientService fireRestClientService;
    private final FireRepository fRepo;

    public FireService(FireRestClientService fireRestClientService,FireRepository fRepo) {
        this.fireRestClientService = fireRestClientService;
        this.fRepo = fRepo;
    }
    public List<FireDto> getFireDtoList() {
        return fireRestClientService.getAllFires();
    }


    public FireDto getNearestFire(FireDto fire) {
        //TODO : get the nearest fire from the list
        FireDto NearestFire = new FireDto();

        return NearestFire;
    }

    public String getFireType(int idf){
        Optional<Fire> f = fRepo.findById(idf);
        if(f.isEmpty()){
            return "No fire found";
        }
        return f.get().getType();
    }
}

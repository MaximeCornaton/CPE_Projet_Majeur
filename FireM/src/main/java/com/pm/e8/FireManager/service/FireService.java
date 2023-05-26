package com.pm.e8.FireManager.service;

import com.google.common.collect.Lists;
import com.pm.e8.FireManager.model.Fire;
import com.pm.e8.FireManager.repository.FireRepository;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import org.springframework.stereotype.Service;
import com.pm.e8.FireManager.model.Fire;

import java.util.ArrayList;
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


    public String getFireType(int idf){
        Optional<Fire> f = fRepo.findById(idf);
        if(f.isEmpty()){
            return "No fire found";
        }
        return f.get().getType();
    }

    public Coord GetFireCoord(int idf) {
        Coord LonLat;
        if (fRepo.findById(idf).isEmpty()) {
            return null;
        } else {
            LonLat = fRepo.findById(idf).get().getCoord();
        }
        return LonLat;
    }

    public Fire GetFireById(int idf) {
        Fire f;
        if (fRepo.findById(idf).isEmpty()) {
            return fireRestClientService.getFire(idf);
        } else {
            f = fRepo.findById(idf).get();
        }
        return f;
    }

    public List<Fire> getFireList() {
        return Lists.newArrayList(fRepo.findAll());
    }
    List<Fire> FiresAround = new ArrayList<Fire>();
    public List<Fire> getFiresAround(int idf, int radius) {
        List<Fire> fList = getFireList();
        Fire fire = null;

        if(fRepo.findById(idf).isEmpty()){
            return null;
        }
        else {
            fire = fRepo.findById(idf).get();
        }
        for(Fire f : fList){
            if(!f.equals(fire)){
                if(distance(fire,f) <= radius){
                    FiresAround.add(f);
                }
            }
        }
        return FiresAround;
    }

    public double distance(Fire f1, Fire f2){
        return Math.sqrt(Math.pow(f1.getCoord().getLon() - f2.getCoord().getLon(),2) + Math.pow(f1.getCoord().getLat() - f2.getCoord().getLat(),2));
    }

}

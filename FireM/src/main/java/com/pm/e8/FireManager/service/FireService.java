package com.pm.e8.FireManager.service;

import com.google.common.collect.Lists;
import com.pm.e8.FireManager.model.Fire;
import com.pm.e8.FireManager.repository.FireRepository;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import com.pm.e8.FireManager.model.Fire;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
        Coord LonLat = new Coord();
        if (fRepo.findById(idf).isEmpty()) {
            return null;
        } else {
            LonLat.setLat(fRepo.findById(idf).get().getLat());
            LonLat.setLon(fRepo.findById(idf).get().getLon());
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
        return getDistanceBetweenCoords(new Coord(f1.getLat(),f1.getLon()),new Coord(f2.getLat(),f2.getLon()));
    }

    public Double getDistanceBetweenCoords(Coord coord1,Coord coord2){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/fire/distance";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("latCoord1", coord1.getLat())
                .queryParam("lonCoord1", coord1.getLon())
                .queryParam("latCoord2", coord2.getLat())
                .queryParam("lonCoord2", coord2.getLon());
        return restTemplate.getForObject(builder.toUriString(), Double.class);
    }

}

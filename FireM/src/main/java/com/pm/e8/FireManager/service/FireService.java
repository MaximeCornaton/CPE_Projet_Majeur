package com.pm.e8.FireManager.service;

import com.google.common.collect.Lists;
import com.pm.e8.FireManager.model.Fire;
import com.pm.e8.FireManager.repository.FireRepository;
import com.project.model.dto.Coord;
import com.project.model.dto.FireDto;
import com.project.model.dto.VehicleDto;
import org.ejml.data.FMatrixSparseTriplet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FireService {
    private final FireRestClientService fireRestClientService;
    private final FireRepository fRepo;
    private VehicleRestService vehicleRestService;

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
    public List<Fire> getFiresAround(int idv, int radius) {
        List<Fire> fList = getFireList();

        VehicleDto v = vehicleRestService.getVehicle(idv);

        for(Fire f : fList){
                if(distance(v,f) <= radius){
                    FiresAround.add(f);
            }
        }
        return FiresAround;
    }

    public double distance(VehicleDto v1, Fire f2){
        return getDistanceBetweenCoords(new Coord(v1.getLat(),v1.getLon()),new Coord(f2.getLat(),f2.getLon()));
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

    public List<FireDto> getTypeFires(String type) {
        List<Fire> fList = getFireList();
        List<FireDto> typeFires = new ArrayList<>();
        for(Fire f : fList){
            FireDto fireDto = FireRestClientService.getFireDto(f.getId());
            if(f.getType().equals(type)){
                typeFires.add(fireDto);
            }
        }
        return typeFires;
    }
}

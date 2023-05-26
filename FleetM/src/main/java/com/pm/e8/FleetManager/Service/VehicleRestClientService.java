package com.pm.e8.FleetManager.Service;

import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class VehicleRestClientService {

    @Value(value = "${team.uuid}")
    private String teamUuid;

    public List<VehicleDto> getTeamVehicles() {
        RestTemplate restTemplate = new RestTemplate();
        String Url = "http://vps.cpe-sn.fr:8081/vehiclebyteam/{teamUuid}";
        VehicleDto[] vehicleDtoList = restTemplate.getForObject(Url, VehicleDto[].class, teamUuid);
        try {
            if(vehicleDtoList == null) {
                throw new Exception("No Vehicle found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return Arrays.asList(vehicleDtoList);
    }

    public VehicleDto getVehicleById(int id) {
        RestTemplate restTemplate = new RestTemplate();
        String Url = "http://vps.cpe-sn.fr:8081/vehicle/{id}";
        VehicleDto vehicleDto = restTemplate.getForObject(Url, VehicleDto.class, id);
        try {
            if(vehicleDto == null) {
                throw new Exception("No Vehicle found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return vehicleDto;
    }

    public ResponseEntity<VehicleDto> moveVehicle(int id, Coord coord) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/vehicle/move/{uuid}/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Coord> request = new HttpEntity<>(coord, headers);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uuid", teamUuid);
        params.put("id", String.valueOf(id));
        ResponseEntity<VehicleDto> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request, VehicleDto.class, params);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("Error");
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }
        return response;
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

    public void updateVehicle(int id, VehicleDto vehicleDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://vps.cpe-sn.fr:8081/vehicle/{uuid}/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VehicleDto> request = new HttpEntity<>(vehicleDto, headers);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uuid", teamUuid);
        params.put("id", String.valueOf(id));
        ResponseEntity<VehicleDto> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request, VehicleDto.class, params);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("Error");
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }
    }
}

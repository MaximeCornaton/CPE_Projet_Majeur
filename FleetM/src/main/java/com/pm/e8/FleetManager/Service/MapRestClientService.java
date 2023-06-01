package com.pm.e8.FleetManager.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pm.e8.FleetManager.model.Vehicle;
import com.project.model.dto.Coord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;

@Service
public class MapRestClientService {
    public String getPolyline(Coord startCoord, Coord endCoord) {
        RestTemplate restTemplate = new RestTemplate();

        String baseUrl = "https://api.mapbox.com/directions/v5/mapbox/driving";
        String accessToken = "pk.eyJ1IjoicGF1bGF1YnJ5IiwiYSI6ImNsaWQ0aW9rZTA4ZGIzZXFmaGdzY2Y3OW8ifQ.Hq_kEdnlh-eTK3HbkhWVRQ";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/{lon1},{lat1};{lon2},{lat2}")
                .queryParam("alternatives", false)
                .queryParam("geometries", "polyline6")
                .queryParam("steps", false)
                .queryParam("access_token", accessToken);

        String url = builder.buildAndExpand(
                startCoord.getLon(),
                startCoord.getLat(),
                endCoord.getLon(),
                endCoord.getLat()
        ).toUriString();

        String jsonResponse = restTemplate.getForObject(url, String.class);
        System.out.println(jsonResponse);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        if (jsonObject.has("routes")) {
            JsonObject firstRoute = jsonObject.getAsJsonArray("routes").get(0).getAsJsonObject();
            if (firstRoute.has("geometry")) {
                return firstRoute.get("geometry").getAsString();
            }
        }
        return null;
    }

    public float getDistance(Vehicle vehicle, Coord endCoord, Coord coordFacility){
        RestTemplate restTemplate = new RestTemplate();

        String baseUrl = "https://api.mapbox.com/directions/v5/mapbox/driving";
        String accessToken = "pk.eyJ1IjoicGF1bGF1YnJ5IiwiYSI6ImNsaWQ0aW9rZTA4ZGIzZXFmaGdzY2Y3OW8ifQ.Hq_kEdnlh-eTK3HbkhWVRQ";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/{lon1},{lat1};{lon2},{lat2};{lon3},{lat3}")
                .queryParam("alternatives", false)
                .queryParam("geometries", "polyline6")
                .queryParam("steps", false)
                .queryParam("access_token", accessToken);

        String url = builder.buildAndExpand(
                vehicle.getLon(),
                vehicle.getLat(),
                endCoord.getLon(),
                endCoord.getLat(),
                coordFacility.getLon(),
                coordFacility.getLat()
        ).toUriString();

        String jsonResponse = restTemplate.getForObject(url, String.class);
        System.out.println(jsonResponse);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        if (jsonObject.has("routes")) {
            JsonObject firstRoute = jsonObject.getAsJsonArray("routes").get(0).getAsJsonObject();
            if (firstRoute.has("geometry")) {
                return firstRoute.get("distance").getAsFloat();
            }
        }
        return -1;
    }
}

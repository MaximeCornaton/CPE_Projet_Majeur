package com.pm.e8.FleetManager.Service;

import com.pm.e8.FleetManager.model.Vehicle;
import com.pm.e8.FleetManager.repository.VehicleRepository;
import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRestClientService vehicleRestClientService;
    private final VehicleRepository vRepo;

    public VehicleService(VehicleRestClientService vehicleRestClientService, VehicleRepository vRepo) {
        this.vehicleRestClientService = vehicleRestClientService;
        this.vRepo = vRepo;
    }

    public ResponseEntity<VehicleDto> moveVehicle(int id, Coord coord) {
        ResponseEntity<VehicleDto> responseEntity = vehicleRestClientService.moveVehicle(id,coord);
        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            VehicleDto vehicleDto = responseEntity.getBody();
            if (vehicleDto != null) {
                return responseEntity;
            } else {
                System.out.println("Failed to move vehicle: Vehicle position does not match the requested coordinates");
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build(); // or choose an appropriate HTTP status
            }
        } else {
            System.out.println("Failed to move vehicle: " + (responseEntity != null ? responseEntity.getStatusCode() : "No response"));
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
    }

    public void moveAllVehicles() {
        List<Vehicle> vehicleToMove = vRepo.findVehicleByFutureLonNotNullAndFutureLatNotNull();
        for(Vehicle vehicle : vehicleToMove) {
            System.out.println(vehicle);
            double distance = this.getDistance(new Coord(vehicle.getLon(), vehicle.getLat()), new Coord(vehicle.getFutureLon(), vehicle.getFutureLat()));
            double speed = vehicle.getType().getMaxSpeed()/3.6;
            if(distance < speed * 1){
                vehicle.setLat(vehicle.getFutureLat());
                vehicle.setLon(vehicle.getFutureLon());
                vehicle.setFutureLat(null);
                vehicle.setFutureLon(null);
                vehicleRestClientService.moveVehicle(vehicle.getId(), new Coord(vehicle.getLon(), vehicle.getLat()));
                vRepo.save(vehicle);
            }
            else{
                double lon1 = Math.toRadians(vehicle.getLon());
                double lon2 = Math.toRadians(vehicle.getFutureLon());
                double lat1 = Math.toRadians(vehicle.getLat());
                double lat2 = Math.toRadians(vehicle.getFutureLat());

                double dLon = (lon2 - lon1);
                double y = Math.sin(dLon) * Math.cos(lat2);
                double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
                double brng = Math.atan2(y, x);
                brng = (brng + 2*Math.PI) % (2*Math.PI);

                double dist = speed * 1;
                double R = 6371e3;
                double lat3 = Math.asin( Math.sin(lat1)*Math.cos(dist/R) + Math.cos(lat1)*Math.sin(dist/R)*Math.cos(brng) );
                double lon3 = lon1 + Math.atan2(Math.sin(brng)*Math.sin(dist/R)*Math.cos(lat1), Math.cos(dist/R)-Math.sin(lat1)*Math.sin(lat3));
                vehicle.setLat(Math.toDegrees(lat3));
                vehicle.setLon(Math.toDegrees(lon3));

                vehicleRestClientService.moveVehicle(vehicle.getId(), new Coord(vehicle.getLon(), vehicle.getLat()));
                System.out.println(vRepo.save(vehicle));
            }
        }
    }

    public float getFuelLevel() {
        return 0.0f;
    }

    public List<VehicleDto> getTeamVehicles() {
        return vehicleRestClientService.getTeamVehicles();
    }

    public VehicleDto getVehicleById(int id) {
        return vehicleRestClientService.getVehicleById(id);
    }

    public int getVehicle() {
        return 0;
    }

    public void startMoving(int id, Coord coord) {
        VehicleDto vehicleDto = this.getVehicleById(id);
        Vehicle vehicle = new Vehicle(vehicleDto);
        vehicle.setFutureLat(coord.getLat());
        vehicle.setFutureLon(coord.getLon());
        vRepo.save(vehicle);
    }

    public void deleteVehicle(int id) {
        vRepo.deleteById(id);
    }

    public double getDistance(Coord coord1, Coord coord2) {
        return vehicleRestClientService.getDistanceBetweenCoords(coord1, coord2);
    }
}

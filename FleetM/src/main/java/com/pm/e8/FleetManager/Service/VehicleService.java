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

    public void moveAllVehicle() {
        List<Vehicle> vehicleToMove = vRepo.findVehicleByLonNotNullAndLatNotNull();
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

    public void moveAllVehicles() {
    }

    public int getVehicle() {
        return 0;
    }
}

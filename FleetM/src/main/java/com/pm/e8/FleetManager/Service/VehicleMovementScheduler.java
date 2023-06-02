package com.pm.e8.FleetManager.Service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VehicleMovementScheduler {
    private final VehicleService vehicleService;

    public VehicleMovementScheduler(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Scheduled(fixedRate = 1000)
    public void moveVehicles() {
        vehicleService.moveAllVehicles();
    }

    //@Scheduled(fixedRate = 5000)
    public void checkVehicles() {
        vehicleService.checkAllVehicles();
    }
}

package com.pm.e8.FleetManager.Controller;

import com.pm.e8.FleetManager.Service.VehicleService;
import com.project.model.dto.VehicleDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleCrt {

    private final VehicleService vehicleService;

    public VehicleCrt(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @RequestMapping(value="/vehicle/{id}/fuel", method= RequestMethod.GET)
    public float getFuelLevel(@RequestParam int id){
        if (vehicleService.getVehicle() == id) {
            return vehicleService.getFuelLevel();
        }else{
            return -1;
        }
    }



    public VehicleDto moveVehicle(int id, int x, int y){
        if (vehicleService.getVehicle() != -1) {
            return vehicleService.moveVehicle(id, x, y);
        }else{
            return null;
        }
    }

}

package com.pm.e8.FleetManager.Controller;

import com.pm.e8.FleetManager.Service.VehicleService;
import com.project.model.dto.Coord;
import com.project.model.dto.VehicleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleCrt {

    private final VehicleService vehicleService;

    public VehicleCrt(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/vehicles")
    public List<VehicleDto> getVehicles(){
        return vehicleService.getTeamVehicles();
    }

    @GetMapping("/vehicle/{id}")
    public VehicleDto getVehicleById(@PathVariable int id){
        return vehicleService.getVehicleById(id);
    }

    @PutMapping("/vehicle/move/{id}")
    public ResponseEntity<VehicleDto> moveVehicle(@PathVariable int id, @RequestBody Coord coord){
        return vehicleService.moveVehicle(id, coord);
    }

    @RequestMapping(value="/vehicle/{id}/fuel", method= RequestMethod.GET)
    public float getFuelLevel(@RequestParam int id){
        if (vehicleService.getVehicle() == id) {
            return vehicleService.getFuelLevel();
        }else{
            return -1;
        }
    }

}

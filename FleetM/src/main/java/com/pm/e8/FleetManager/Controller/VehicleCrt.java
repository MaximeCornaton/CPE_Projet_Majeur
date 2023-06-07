package com.pm.e8.FleetManager.Controller;

import com.pm.e8.FleetManager.Service.VehicleRestClientService;
import com.pm.e8.FleetManager.Service.VehicleService;
import com.pm.e8.FleetManager.model.Vehicle;
import com.project.model.dto.Coord;
import com.project.model.dto.FireType;
import com.project.model.dto.VehicleDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class VehicleCrt {

    private final VehicleService vehicleService;
    private final VehicleRestClientService vehicleRestClientService;

    public VehicleCrt(VehicleService vehicleService, VehicleRestClientService vehicleRestClientService) {
        this.vehicleService = vehicleService;
        this.vehicleRestClientService = vehicleRestClientService;
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
    public void moveVehicle(@PathVariable int id, @RequestBody Coord coord){
        vehicleService.startMoving(id, coord);
    }

    @GetMapping("/vehicle/distance")
    public double getDistance(@RequestParam double latCoord1, double lonCoord1, double latCoord2, double lonCoord2){
        return vehicleService.getDistance(new Coord(lonCoord1, latCoord1), new Coord(lonCoord2, latCoord2));
    }

    @PutMapping("/vehicle/{id}/liquidType")
    public void updateVehicleLiquidType(@PathVariable int id, @RequestParam String liquidType){
        vehicleService.updateVehicleLiquidType(id, liquidType);
    }

    /*@RequestMapping(value="/vehicle/{id}/fuel", method= RequestMethod.GET)
    public float getFuelLevel(@RequestParam int id){
        if (vehicleService.getVehicleById(id) == id) {
            return vehicleService.getFuelLevel();
        }else{
            return -1;
        }
    }*/

    @RequestMapping(value="/vehicle/{id}/enoughFuel", method= RequestMethod.GET)
    public boolean enoughFuel(@PathVariable int id, @RequestParam int fireDtoId, @RequestParam int facilityDtoId){
        return vehicleService.enoughFuel(vehicleService.getVehicleById(id), fireDtoId, facilityDtoId);
    }

    @PostMapping("/vehicles/{vehicleId}/return")
    public void returnVehicle(@PathVariable int vehicleId){
        vehicleService.returnVehicle(vehicleId);
    }

    @DeleteMapping("/vehicle/{id}/delete")
    public void deleteVehicle(@PathVariable int id){
        vehicleService.deleteVehicle(id);
    }

    @PostMapping("/vehicle/add")
    public void addVehicle(@RequestBody VehicleDto vehicleDto){
        vehicleRestClientService.addVehicle(vehicleDto);
    }

    @GetMapping("/vehicle/liquid/type")
    public Map<FireType,List<VehicleDto>> getFireTypeVehicleMap(){
        return vehicleService.getFireTypeVehicleMap();
    }

}

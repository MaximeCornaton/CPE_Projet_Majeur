package com.pm.e8.FleetManager.Service;

import com.pm.e8.FleetManager.exception.NotEnoughFuelException;
import com.pm.e8.FleetManager.model.Coordonnees;
import com.pm.e8.FleetManager.model.Vehicle;
import com.pm.e8.FleetManager.repository.CoordonneesRepository;
import com.pm.e8.FleetManager.repository.VehicleRepository;
import com.pm.e8.FleetManager.tools.PolylineSplitter;
import com.project.model.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vRepo;
    private final CoordonneesRepository cRepo;
    private final VehicleRestClientService vehicleRestClientService;
    private final FireRestClientService fireRestClientService;
    private final FacilityRestClientService facilityRestClientService;
    private final MapRestClientService mapRestClientService;

    private final List<Vehicle> currentListVehicle;

    public VehicleService(VehicleRestClientService vehicleRestClientService, VehicleRepository vRepo, CoordonneesRepository cRepo, FireRestClientService fireRestClientService, FacilityRestClientService facilityRestClientService, MapRestClientService mapRestClientService) {
        this.vehicleRestClientService = vehicleRestClientService;
        this.vRepo = vRepo;
        this.cRepo = cRepo;
        this.fireRestClientService = fireRestClientService;
        this.facilityRestClientService = facilityRestClientService;
        this.mapRestClientService = mapRestClientService;
        this.currentListVehicle = new ArrayList<>();
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

    public List<Vehicle> getPumpers(){
        return vRepo.findByType("PUMPER_TRUCK");
    }

    public Vehicle findVehicleById(int id){
        return vRepo.findById(id);
    }

    public void moveAllVehicles() {
        List<Vehicle> vehicleToMove = vRepo.findByCoordonneesListIsNotEmpty();
        for(Vehicle vehicle : vehicleToMove) {
            Coordonnees coord = cRepo.findTopByVehicleIdOrderByIdAsc(vehicle.getId()).orElseThrow();
            Vehicle newVehicle = new Vehicle(Objects.requireNonNull(vehicleRestClientService.moveVehicle(vehicle.getId(), new Coord(coord.getLon(), coord.getLat())).getBody()));
            cRepo.delete(coord);
            //System.out.println(vRepo.save(newVehicle));
        }
    }

    public float getFuelLevel(Vehicle vehicle) {
        return vehicle.getFuel();
    }

    public List<VehicleDto> getTeamVehicles() {
        return vehicleRestClientService.getTeamVehicles();
    }

    public VehicleDto getVehicleById(int id) {
        return vehicleRestClientService.getVehicleById(id);
    }

    public List<Vehicle> getCurrentListVehicle() {
        return currentListVehicle;
    }

    public boolean isVehicleInMovement(int id) {
        return currentListVehicle.stream().anyMatch(v -> v.getId() == id && v.isInMovement());
    }

    public void setCurrentListVehicle(Vehicle currentVehicle) {
        this.currentListVehicle.add(currentVehicle);
    }
    public void startMoving(int id, Coord coord) {
        VehicleDto vehicleDto = this.getVehicleById(id);
        Vehicle vehicle = new Vehicle(vehicleDto);
        String polyline = mapRestClientService.getPolyline(new Coord(vehicle.getLon(),vehicle.getLat()),coord);
        if(!hasEnoughFuel(vehicle,coord)){
            throw new NotEnoughFuelException("Not enough fuel");
        }
        List<Coord> coordList = PolylineSplitter.cutPolyline(polyline, vehicle.getType().getMaxSpeed()/1000);
        List<Coordonnees> futurCoordList = new ArrayList<>();
        for(Coord c : coordList){
            Coordonnees tempCoord = new Coordonnees(c.getLon(),c.getLat());
            tempCoord.setVehicle(vehicle);
            futurCoordList.add(tempCoord);
        }

        Coordonnees lastCoord = new Coordonnees(coord.getLon(),coord.getLat());
        lastCoord.setVehicle(vehicle);
        futurCoordList.add(lastCoord);

        vehicle.setCoordonnees(futurCoordList);
        vehicle.setInMovement(true);
        vRepo.save(vehicle);
    }

    private Vehicle GetVehicleById(int id) {
        return vRepo.findById(id);
    }

    public void deleteVehicle(int id) {
        vRepo.deleteById(id);
    }

    public double getDistance(Coord coord1, Coord coord2) {
        return vehicleRestClientService.getDistanceBetweenCoords(coord1, coord2);
    }

    public double getDistanceRealizable(Vehicle vehicle) {
        double fuelLevel = this.getFuelLevel(vehicle);
        VehicleType vehicleType = vehicle.getType();
        double fuelConsumption = vehicleType.getFuelConsumption();
        return fuelLevel/(fuelConsumption/100000);
    }

    public boolean enoughFuel(VehicleDto vehicleDto, int fireDtoId, int facilityDtoId) {
        FireDto fireDto = fireRestClientService.getFireDtoById(fireDtoId);
        FacilityDto facilityDto = facilityRestClientService.getFacilityDtoById(facilityDtoId);
        double distancePosFire = this.getDistance( new Coord(vehicleDto.getLon(), vehicleDto.getLat()), new Coord(fireDto.getLon(), fireDto.getLat()));
        double distanceFireFacility = this.getDistance( new Coord(fireDto.getLon(), fireDto.getLat()), new Coord(facilityDto.getLon(), facilityDto.getLat()));
        return (distancePosFire + distanceFireFacility) < this.getDistanceRealizable(new Vehicle(vehicleDto));
    }

    public void setCurrentListVehicle() {
        List<VehicleDto> vehicleDtoList = vehicleRestClientService.getTeamVehicles();
        if (currentListVehicle.isEmpty()) {
            for (VehicleDto vehicleDto : vehicleDtoList) {
                Vehicle temp = new Vehicle(vehicleDto);
                currentListVehicle.add(temp);
            }
        }else{
            List<Integer> idList = new ArrayList<>();
            for (Vehicle vehicle : currentListVehicle) {
                idList.add(vehicle.getId());
            }
            for (VehicleDto vehicleDto : vehicleDtoList) {
                Vehicle temp = new Vehicle(vehicleDto);
                int id = temp.getId();
                if (!idList.contains(id)){
                    currentListVehicle.add(temp);
                }
            }
        }
    }

    public void checkAllVehicles(){
        setCurrentListVehicle();
        for(Vehicle vehicle : currentListVehicle){
            System.out.println("Liste de vehicle: \n" + currentListVehicle);
            VehicleDto vehicleDto = this.getVehicleById(vehicle.getId());
            FacilityDto facilityDto = facilityRestClientService.getFacility(vehicleDto.getFacilityRefID());
            if(vehicleDto.getLiquidQuantity() < 1 && vehicleDto.getLon() != facilityDto.getLon() && vehicleDto.getLat() != facilityDto.getLat() && !vehicle.isInMovement()){ //il faut trouver un moyen de regarder s'il est en mouvement, sinon il recrée une list de coordonnées qui sera re exéxutée apres son premier trahet
               System.out.println(vehicle.getId() + " retourne à la caserne et n'est pas en mouvement: " + vehicle.isInMovement());
                this.backToFacility(vehicleDto,facilityDto);
                vehicle.setInMovement(true);
                vRepo.save(vehicle);
                System.out.println(vehicle.getId() + " retourne à la caserne et est en mouvement: " + vehicle.isInMovement());

                //this.startMoving(vehicleDto.getId(),new Coord(facilityDto.getLon(),facilityDto.getLat()));
            }
            if(vehicleDto.getLiquidQuantity() > vehicleDto.getType().getLiquidCapacity()-1 && vehicleDto.getLon() == facilityDto.getLon() && vehicleDto.getLat() == facilityDto.getLat()){
                /*currentListVehicle.remove(vehicle);
                setCurrentListVehicle();*/
                vehicle.setInMovement(false);
                System.out.println("Je cherche un feu");
                this.findFire(vehicleDto);
            }
        }
    }

    private void findFire(VehicleDto vehicleDto) {
        List<FireDto> fireDtoList = fireRestClientService.getAllFires();
        if(fireDtoList.isEmpty()){
            return;
        }
        FireDto fireDto = fireDtoList.get(0);
        double distance = this.getDistance(new Coord(vehicleDto.getLon(),vehicleDto.getLat()),new Coord(fireDto.getLon(),fireDto.getLat()));
        for(FireDto fireDto1 : fireDtoList){
            distance = this.getDistance(new Coord(vehicleDto.getLon(),vehicleDto.getLat()),new Coord(fireDto.getLon(),fireDto.getLat()));
            if(this.getDistance(new Coord(vehicleDto.getLon(),vehicleDto.getLat()),new Coord(fireDto1.getLon(),fireDto1.getLat())) < distance){
                fireDto = fireDto1;
            }
        }
        this.startMoving(vehicleDto.getId(),new Coord(fireDto.getLon(),fireDto.getLat()));
    }

    private void backToFacility(VehicleDto vehicleDto, FacilityDto facilityDto) {
        System.out.println("Je retourne à la caserne");
        Coord coord = new Coord(facilityDto.getLon(), facilityDto.getLat());
        startMoving(vehicleDto.getId(), coord);
    }

    public void updateVehicleLiquidType(int id, String liquidType) {
        VehicleDto vehicleDto = vehicleRestClientService.getVehicleById(id);
        FacilityDto facilityDto = facilityRestClientService.getFacility(vehicleDto.getFacilityRefID());

        if(facilityDto.getLat() == vehicleDto.getLat() && facilityDto.getLon() == vehicleDto.getLon()) {
            vehicleDto.setLiquidType(LiquidType.valueOf(liquidType));
            vehicleRestClientService.updateVehicle(id,vehicleDto);
        }
    }
    public boolean hasEnoughFuel(Vehicle vehicle, Coord coord) {
        FacilityDto facility = facilityRestClientService.getFacility(vehicle.getFacilityRefID());
        float distance = mapRestClientService.getDistance(vehicle,coord,new Coord(facility.getLon(),facility.getLat()));
        return distance < getDistanceRealizable(vehicle) + 3;
    }


    public void returnVehicle(int vehicleId) {
        VehicleDto vehicleDto = vehicleRestClientService.getVehicleById(vehicleId);
        FacilityDto facilityDto = facilityRestClientService.getFacility(vehicleDto.getFacilityRefID());
        Coord coord = new Coord(facilityDto.getLon(), facilityDto.getLat());
        startMoving(vehicleDto.getId(), coord);

    }
}
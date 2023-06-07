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

import java.util.*;

@Service
public class VehicleService {

    private final VehicleRepository vRepo;
    private final CoordonneesRepository cRepo;
    private final VehicleRestClientService vehicleRestClientService;
    private final FireRestClientService fireRestClientService;
    private final FacilityRestClientService facilityRestClientService;
    private final MapRestClientService mapRestClientService;

    private final List<Vehicle> currentListVehicle;
    private List<Integer> fireAvailable;

    public VehicleService(VehicleRestClientService vehicleRestClientService, VehicleRepository vRepo, CoordonneesRepository cRepo, FireRestClientService fireRestClientService, FacilityRestClientService facilityRestClientService, MapRestClientService mapRestClientService) {
        this.vehicleRestClientService = vehicleRestClientService;
        this.vRepo = vRepo;
        this.cRepo = cRepo;
        this.fireRestClientService = fireRestClientService;
        this.facilityRestClientService = facilityRestClientService;
        this.mapRestClientService = mapRestClientService;
        this.currentListVehicle = new ArrayList<>();
        this.fireAvailable = new ArrayList<>();
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

    private List<Integer> getFireAvailable(){
        return this.fireAvailable;
    }

    private List<Vehicle> getCurrentListVehicle(){
        return this.currentListVehicle;
    }

    public List<VehicleDto> getTeamVehicles() {
        return vehicleRestClientService.getTeamVehicles();
    }

    public VehicleDto getVehicleById(int id) {
        return vehicleRestClientService.getVehicleById(id);
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
            VehicleDto vehicleDto = this.getVehicleById(vehicle.getId());
            FacilityDto facilityDto = facilityRestClientService.getFacility(vehicleDto.getFacilityRefID());
            if(vehicleDto.getLiquidQuantity() < 1 && vehicleDto.getLon() != facilityDto.getLon() && vehicleDto.getLat() != facilityDto.getLat() && !vehicle.isInMovement()){ //il faut trouver un moyen de regarder s'il est en mouvement, sinon il recrée une list de coordonnées qui sera re exéxutée apres son premier trahet
               System.out.println(vehicle.getId() + " retourne à la caserne et n'est pas en mouvement: " + vehicle.isInMovement());
                this.backToFacility(vehicleDto,facilityDto);
                vehicle.setInMovement(true);
                vRepo.save(vehicle);
                System.out.println(vehicle.getId() + " retourne à la caserne et est en mouvement: " + vehicle.isInMovement());
            }
            if(vehicleDto.getLiquidQuantity() > vehicleDto.getType().getLiquidCapacity()-1 && vehicleDto.getLon() == facilityDto.getLon() && vehicleDto.getLat() == facilityDto.getLat()){
                vehicle.setInMovement(false);
                System.out.println("Je cherche un feu");
                this.findFire(vehicleDto);
            }else if (vehicle.getTarget() == -1){
                findFire(vehicleDto);
                System.out.println(vehicle.getId() + " à trouvé un feu: le " + vehicle.getTarget());
            } else if (vehicle.getTarget() != -1) {
                Exception e = new Exception();
                if (fireRestClientService.getFireDtoById(vehicle.getTarget()) == null){
                    System.out.println("Le feu sru lequel était " + vehicle.getId() + " n'existe plus");
                    vehicle.setTarget(-1);
                    vRepo.save(vehicle);
                }
            }
        }
    }

    private void findFire(VehicleDto vehicleDto) {
        List<FireDto> fireDtoList = fireRestClientService.getAllFires();
        if(fireDtoList.isEmpty()){
            return;
        }
        int id = 0;
        boolean found = false;
        while (!found){
            FireDto fireDto = fireDtoList.get(id);
            if (fireAvailable.isEmpty()){
                fireAvailable.add(fireDto.getId());
                found = true;
                try {
                    if (fireRestClientService.getFireDtoById(fireDto.getId()).getType().equals("E_Electric")){
                        found = true;
                    }
                }
                catch (Exception e){
                    id++;
                }

            }else if (!fireAvailable.contains(fireDto.getId())){
                fireAvailable.add(fireDto.getId());
                found = true;
            }else{
                id++;
            }
        }
        Vehicle vehicle = null;
        for (Vehicle vehicleC: currentListVehicle) {
            if (vehicleC.getId() == vehicleDto.getId()){
                vehicle = vehicleC;
            }
        }
        FireDto fireDto = fireDtoList.get(id);
        if (vehicle != null) {
            vehicle.setTarget(fireDto.getId());
        }
        System.out.println(vehicleDto.getId() + " va au feu: " + fireDto.getId());
        this.startMoving(vehicleDto.getId(),new Coord(fireDto.getLon(),fireDto.getLat()));
    }

    public void clearFireAvailable(){
        for (int i=0; i<fireAvailable.size(); i++) {
            if (fireRestClientService.getFireDtoById(fireAvailable.get(i)) == null) {
                fireAvailable.remove(i);
            } else if (fireRestClientService.getFireDtoById(fireAvailable.get(i)).getType().equals("E_ELECTRIC")) {
                fireAvailable.remove(i);
            }
        }
        for (Vehicle vehicle : currentListVehicle){
            if (vehicle.getTarget() != -1){
                for (int i=0; i<fireAvailable.size(); i++){
                    if (fireRestClientService.getFireDtoById(fireAvailable.get(i)) == null){
                        fireAvailable.remove(i);
                    }
                }
            }
        }
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
        startMoving(vehicleDto.getId(), coord); //commentaire
    }

    public void deleteVehicle(int id) {
        VehicleDto vehicleDto = vehicleRestClientService.getVehicleById(id);
        FacilityDto facilityDto = facilityRestClientService.getFacilityDtoById(vehicleDto.getFacilityRefID());
        backToFacility(vehicleDto,facilityDto);
        while(vehicleDto.getLat() != facilityDto.getLat() && vehicleDto.getLon() != facilityDto.getLon()){

        }
        vehicleRestClientService.deleteVehicleRest(id);
    }

    public Map<FireType, List<VehicleDto>> getFireTypeVehicleMap() {
        Map<FireType,List<VehicleDto>> fireTypeVehicleMap = new HashMap<>();
        List<VehicleDto> vehicleDtoList = this.getTeamVehicles();

        for(FireType fireType : FireType.values()){
            fireTypeVehicleMap.put(fireType,new ArrayList<>());
            for(VehicleDto v:vehicleDtoList){
                float efficiency = v.getLiquidType().getEfficiency(String.valueOf(fireType));
                if(efficiency > 0){
                    fireTypeVehicleMap.get(fireType).add(v);
                }
            }
        }
        return fireTypeVehicleMap;
    }
}
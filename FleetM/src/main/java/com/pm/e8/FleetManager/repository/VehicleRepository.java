package com.pm.e8.FleetManager.repository;

import com.pm.e8.FleetManager.model.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle,Integer> {
    List<Vehicle> findVehicleByFutureLonNotNullAndFutureLatNotNull();
    List<Vehicle> findByCoordonneesListIsNotEmpty();

    List<Vehicle> findByType(String type);
}

package com.pm.e8.FleetManager.repository;

import com.pm.e8.FleetManager.model.Coordonnees;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CoordonneesRepository extends CrudRepository<Coordonnees,Long> {
    Optional<Coordonnees> findTopByVehicleIdOrderByIdAsc(Integer vehicleId);
}

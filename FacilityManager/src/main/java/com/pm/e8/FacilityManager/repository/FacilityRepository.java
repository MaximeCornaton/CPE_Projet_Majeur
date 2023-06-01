package com.pm.e8.FacilityManager.repository;

import com.pm.e8.FacilityManager.model.Facility;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends CrudRepository<Facility, Integer> {
    //@Query(value="SELECT * FROM Facility WHERE Facility.teamUuid = '4c848170-4287-4c5f-9e1b-3d16ae2d4470'", nativeQuery = true )
    //List<Facility> findByteam_uuid();
    List<Facility> findByTeamUuid(String teamUuid);

    Object findByName(String name);
}

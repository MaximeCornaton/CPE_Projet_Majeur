package com.pm.e8.Intervention.repository;

import com.pm.e8.Intervention.model.Intervention;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterventionRepository extends CrudRepository<Intervention,Long> {

}

package com.pm.e8.Intervention.repository;

import com.pm.e8.Intervention.model.Intervention;
import com.pm.e8.Intervention.model.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterventionRepository extends CrudRepository<Intervention,Long> {

    List<Intervention> findAllByStatus(Status status);
}

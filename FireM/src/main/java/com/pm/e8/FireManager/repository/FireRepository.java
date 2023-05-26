package com.pm.e8.FireManager.repository;
import com.pm.e8.FireManager.model.Fire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FireRepository extends CrudRepository<Fire, Integer>{

}

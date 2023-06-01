package com.pm.e8.FleetManager.Controller;

import com.pm.e8.FleetManager.exception.NotEnoughFuelException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VehicleCrtAdvice {
    @ExceptionHandler(NotEnoughFuelException.class)
    public ResponseEntity<?> handleNotEnoughFuelException(NotEnoughFuelException e){
        return new ResponseEntity<>(e.getMessage(), null, 501);
    }
}

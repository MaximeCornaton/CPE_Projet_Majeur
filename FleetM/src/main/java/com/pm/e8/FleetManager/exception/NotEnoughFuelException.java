package com.pm.e8.FleetManager.exception;

public class NotEnoughFuelException extends RuntimeException{
    public NotEnoughFuelException(String message) {
        super(message);
    }
}

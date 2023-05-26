package com.pm.e8.FleetManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FleetManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FleetManagerApplication.class, args);
	}

}

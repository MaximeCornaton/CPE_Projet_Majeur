package com.pm.e8.FireManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FireManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FireManagerApplication.class, args);
	}

}

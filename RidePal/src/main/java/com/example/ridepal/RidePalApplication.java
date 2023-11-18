package com.example.ridepal;

import com.example.ridepal.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.ridepal.repositories")
@EntityScan("com.example.ridepal.models")
public class RidePalApplication {

    public static void main(String[] args) {
        SpringApplication.run(RidePalApplication.class, args);
    }

}

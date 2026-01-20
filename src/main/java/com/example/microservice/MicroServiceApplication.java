package com.example.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MicroServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroServiceApplication.class, args);
    }
}

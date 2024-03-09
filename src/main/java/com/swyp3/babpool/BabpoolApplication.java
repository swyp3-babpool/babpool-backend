package com.swyp3.babpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BabpoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(BabpoolApplication.class, args);
    }

}

package com.rescureat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.rescureat.security.JwtProperties;

/**
 * RescuEat backend application.
 */
@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class RescuEatApplication {

    public static void main(String[] args) {
        SpringApplication.run(RescuEatApplication.class, args);
    }
}

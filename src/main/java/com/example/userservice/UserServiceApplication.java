package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc

public class UserAndRolesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAndRolesApplication.class, args);
    }

}

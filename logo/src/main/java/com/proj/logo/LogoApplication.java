package com.proj.logo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LogoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogoApplication.class, args);
    }
}

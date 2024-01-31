package com.proj.emailkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EmailKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailKafkaApplication.class, args);
    }
}

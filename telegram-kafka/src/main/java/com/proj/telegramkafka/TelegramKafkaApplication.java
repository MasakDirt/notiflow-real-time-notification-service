package com.proj.telegramkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TelegramKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramKafkaApplication.class, args);
    }
}

package com.proj.telegramkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TelegramKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramKafkaApplication.class, args);
    }
}

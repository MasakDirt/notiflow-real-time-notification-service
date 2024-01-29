package com.proj.telegramkafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfig {

    @Value("${telegram.bot-token}")
    private String token;

    @Bean
    public String token() {
        return token;
    }
}

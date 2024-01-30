package com.proj.telegramkafka;

import com.proj.telegramkafka.service.TelegramUserService;
import com.proj.telegramkafka.telegrambot.NotiflowBot;
import com.proj.telegramkafka.exception.NotiflowBotException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@SpringBootApplication
@EnableEurekaClient
@AllArgsConstructor
public class TelegramKafkaApplication {

    private final String token;
    private final TelegramUserService telegramUserService;

    public static void main(String[] args) {
        SpringApplication.run(TelegramKafkaApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> runTelegramBot();
    }

    private void runTelegramBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new NotiflowBot(token, telegramUserService));
            log.info("NotiflowBot started successfully!");
        } catch (Exception exception) {
            log.info("Exception was throwing while bot runs.");
            throw new NotiflowBotException("Something go wrong while bot runs.");
        }
    }
}

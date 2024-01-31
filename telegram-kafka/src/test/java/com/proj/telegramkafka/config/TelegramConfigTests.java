package com.proj.telegramkafka.config;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TelegramConfigTests {

    private final TelegramConfig telegramConfig;

    @Autowired
    public TelegramConfigTests(TelegramConfig telegramConfig) {
        this.telegramConfig = telegramConfig;
    }

    @Test
    public void testNotNullTelegramTopic() {
        AssertionsForClassTypes.assertThat(telegramConfig.token()).isNotNull();
    }

    @Test
    public void testTopicName() {
        String actualToken = telegramConfig.token();
        Assertions.assertTrue(actualToken.contains("678562"));
    }
}

package com.proj.telegramkafka.kafka.config;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TelegramTopicConfigTests {

    private final TelegramTopicConfig telegramTopicConfig;

    @Autowired
    public TelegramTopicConfigTests(TelegramTopicConfig telegramTopicConfig) {
        this.telegramTopicConfig = telegramTopicConfig;
    }

    @Test
    public void testNotNullTelegramTopic() {
        AssertionsForClassTypes.assertThat(telegramTopicConfig.telegramTopic()).isNotNull();
    }

    @Test
    public void testTopicName() {
        String expectedTopicName = "telegram";
        String actualTopicName = telegramTopicConfig.telegramTopic().name();
        Assertions.assertEquals(expectedTopicName, actualTopicName);
    }
}

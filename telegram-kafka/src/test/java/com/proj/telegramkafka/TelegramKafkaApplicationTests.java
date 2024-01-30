package com.proj.telegramkafka;

import com.proj.telegramkafka.config.TelegramConfig;
import com.proj.telegramkafka.controller.MessageController;
import com.proj.telegramkafka.kafka.config.TelegramConsumerConfig;
import com.proj.telegramkafka.kafka.config.TelegramProducerConfig;
import com.proj.telegramkafka.kafka.config.TelegramTopicConfig;
import com.proj.telegramkafka.kafka.listeners.KafkaListeners;
import com.proj.telegramkafka.repository.TelegramUserRepository;
import com.proj.telegramkafka.service.TelegramUserService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TelegramKafkaApplicationTests {
    private final TelegramConfig telegramConfig;
    private final MessageController messageController;
    private final TelegramConsumerConfig telegramConsumerConfig;
    private final TelegramProducerConfig telegramProducerConfig;
    private final TelegramTopicConfig telegramTopicConfig;
    private final KafkaListeners kafkaListeners;
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramUserService telegramUserService;

    @Autowired
    public TelegramKafkaApplicationTests(
            TelegramConfig telegramConfig, MessageController messageController,
            TelegramConsumerConfig telegramConsumerConfig, TelegramProducerConfig telegramProducerConfig,
            TelegramTopicConfig telegramTopicConfig, KafkaListeners kafkaListeners,
            TelegramUserRepository telegramUserRepository, TelegramUserService telegramUserService) {

        this.telegramConfig = telegramConfig;
        this.messageController = messageController;
        this.telegramConsumerConfig = telegramConsumerConfig;
        this.telegramProducerConfig = telegramProducerConfig;
        this.telegramTopicConfig = telegramTopicConfig;
        this.kafkaListeners = kafkaListeners;
        this.telegramUserRepository = telegramUserRepository;
        this.telegramUserService = telegramUserService;
    }

    @Test
    public void testInjectedComponents() {
        AssertionsForClassTypes.assertThat(telegramConfig).isNotNull();
        AssertionsForClassTypes.assertThat(messageController).isNotNull();
        AssertionsForClassTypes.assertThat(telegramConsumerConfig).isNotNull();
        AssertionsForClassTypes.assertThat(telegramProducerConfig).isNotNull();
        AssertionsForClassTypes.assertThat(telegramTopicConfig).isNotNull();
        AssertionsForClassTypes.assertThat(kafkaListeners).isNotNull();
        AssertionsForClassTypes.assertThat(telegramUserRepository).isNotNull();
        AssertionsForClassTypes.assertThat(telegramUserService).isNotNull();
    }
}

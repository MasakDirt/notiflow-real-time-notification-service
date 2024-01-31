package com.proj.telegramkafka.kafka.config;

import com.proj.telegramkafka.model.NotificationData;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TelegramConsumerConfigTests {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapService;

    private final TelegramConsumerConfig telegramConsumerConfig;
    private final ConsumerFactory<String, NotificationData> expectedConsumerFactory;
    private final KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, NotificationData>> expectedContainerFactory;

    @Autowired
    public TelegramConsumerConfigTests(TelegramConsumerConfig telegramConsumerConfig,
                                       ConsumerFactory<String, NotificationData> expectedConsumerFactory,
                                       KafkaListenerContainerFactory<
                                               ConcurrentMessageListenerContainer<String, NotificationData>> containerFactory) {
        this.telegramConsumerConfig = telegramConsumerConfig;
        this.expectedConsumerFactory = expectedConsumerFactory;
        this.expectedContainerFactory = containerFactory;
    }

    @Test
    public void testInjectedComponentForNull() {
        AssertionsForClassTypes.assertThat(telegramConsumerConfig).isNotNull();
        AssertionsForClassTypes.assertThat(expectedConsumerFactory).isNotNull();
        AssertionsForClassTypes.assertThat(expectedContainerFactory).isNotNull();
    }

    @Test
    public void testValidConsumerFactory() {
        ConsumerFactory<String, NotificationData> actual = telegramConsumerConfig.consumerFactory();
        assertEquals(expectedConsumerFactory, actual);
    }

    @Test
    public void testValidConsumerConfig() {
        Map<String, Object> expectedConsumerConfig = new HashMap<>();
        expectedConsumerConfig.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapService);
        expectedConsumerConfig.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        expectedConsumerConfig.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        Map<String, Object> actualConsumerConfig = telegramConsumerConfig.consumerConfig();

        assertEquals(expectedConsumerConfig, actualConsumerConfig);
    }

    @Test
    public void testValidContainerFactory() {
        KafkaListenerContainerFactory<
                ConcurrentMessageListenerContainer<String, NotificationData>> actual =
                telegramConsumerConfig.factory(telegramConsumerConfig.consumerFactory());
        assertEquals(expectedContainerFactory, actual);
    }
}

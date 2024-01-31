package com.proj.telegramkafka.kafka.config;

import com.proj.telegramkafka.model.NotificationData;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TelegramProducerConfigTests {

    @Value("${spring.kafka.bootstrap-servers}")
    private  String bootstrapService;

    private final TelegramProducerConfig telegramProducerConfig;
    private final ProducerFactory<String, NotificationData> expectedProducerFactory;
    private final KafkaTemplate<String, NotificationData> expectedKafkaTemplate;

    @Autowired
    public TelegramProducerConfigTests(TelegramProducerConfig telegramProducerConfig,
                                       ProducerFactory<String, NotificationData> producerFactory,
                                       KafkaTemplate<String, NotificationData> kafkaTemplate) {
        this.telegramProducerConfig = telegramProducerConfig;
        this.expectedProducerFactory = producerFactory;
        this.expectedKafkaTemplate = kafkaTemplate;
    }

    @Test
    public void testInjectedComponentForNull() {
        AssertionsForClassTypes.assertThat(telegramProducerConfig).isNotNull();
        AssertionsForClassTypes.assertThat(expectedProducerFactory).isNotNull();
        AssertionsForClassTypes.assertThat(expectedKafkaTemplate).isNotNull();
    }

    @Test
    public void testValidProducerFactory() {
        ProducerFactory<String, NotificationData> actual = telegramProducerConfig.producerFactory();
        assertEquals(expectedProducerFactory, actual);
    }

    @Test
    public void testValidProducerConfig() {
        Map<String, Object> expectedConsumerConfig = new HashMap<>();
        expectedConsumerConfig.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapService);
        expectedConsumerConfig.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        expectedConsumerConfig.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        Map<String, Object> actualConsumerConfig = telegramProducerConfig.producerConfig();

        assertEquals(expectedConsumerConfig, actualConsumerConfig);
    }

    @Test
    public void testValidKafkaTemplate() {
        KafkaTemplate<String, NotificationData> actual =
                telegramProducerConfig.kafkaTemplate(telegramProducerConfig.producerFactory());
        assertEquals(expectedKafkaTemplate, actual);
    }
}

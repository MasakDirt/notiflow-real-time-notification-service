package com.proj.emailkafka.kafka.config;

import com.proj.emailkafka.model.NotificationData;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmailProducerConfigTests {

    @Value("${spring.kafka.bootstrap-servers}")
    private  String bootstrapService;

    private final EmailProducerConfig emailProducerConfig;
    private final ProducerFactory<String, NotificationData> expectedProducerFactory;
    private final KafkaTemplate<String, NotificationData> expectedKafkaTemplate;

    @Autowired
    public EmailProducerConfigTests(EmailProducerConfig emailProducerConfig,
                                       ProducerFactory<String, NotificationData> producerFactory,
                                       KafkaTemplate<String, NotificationData> kafkaTemplate) {
        this.emailProducerConfig = emailProducerConfig;
        this.expectedProducerFactory = producerFactory;
        this.expectedKafkaTemplate = kafkaTemplate;
    }

    @Test
    public void testInjectedComponentForNull() {
        AssertionsForClassTypes.assertThat(emailProducerConfig).isNotNull();
        AssertionsForClassTypes.assertThat(expectedProducerFactory).isNotNull();
        AssertionsForClassTypes.assertThat(expectedKafkaTemplate).isNotNull();
    }

    @Test
    public void testValidProducerFactory() {
        ProducerFactory<String, NotificationData> actual = emailProducerConfig.producerFactory();
        assertEquals(expectedProducerFactory, actual);
    }

    @Test
    public void testValidProducerConfig() {
        Map<String, Object> expectedConsumerConfig = new HashMap<>();
        expectedConsumerConfig.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapService);
        expectedConsumerConfig.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        expectedConsumerConfig.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        Map<String, Object> actualConsumerConfig = emailProducerConfig.producerConfig();

        assertEquals(expectedConsumerConfig, actualConsumerConfig);
    }

    @Test
    public void testValidKafkaTemplate() {
        KafkaTemplate<String, NotificationData> actual =
                emailProducerConfig.kafkaTemplate(emailProducerConfig.producerFactory());
        assertEquals(expectedKafkaTemplate, actual);
    }
}

package com.proj.emailkafka.kafka.config;

import com.proj.emailkafka.model.NotificationData;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmailConsumerConfigTests {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapService;

    private final EmailConsumerConfig emailConsumerConfig;
    private final ConsumerFactory<String, NotificationData> expectedConsumerFactory;
    private final KafkaListenerContainerFactory<
                ConcurrentMessageListenerContainer<String, NotificationData>> expectedContainerFactory;

    @Autowired
    public EmailConsumerConfigTests(EmailConsumerConfig emailConsumerConfig,
                                       ConsumerFactory<String, NotificationData> expectedConsumerFactory,
                                       KafkaListenerContainerFactory<
                                               ConcurrentMessageListenerContainer<String, NotificationData>> containerFactory) {
        this.emailConsumerConfig = emailConsumerConfig;
        this.expectedConsumerFactory = expectedConsumerFactory;
        this.expectedContainerFactory = containerFactory;
    }

    @Test
    public void testInjectedComponentForNull() {
        AssertionsForClassTypes.assertThat(emailConsumerConfig).isNotNull();
        AssertionsForClassTypes.assertThat(expectedConsumerFactory).isNotNull();
        AssertionsForClassTypes.assertThat(expectedContainerFactory).isNotNull();
    }

    @Test
    public void testValidConsumerFactory() {
        ConsumerFactory<String, NotificationData> actual = emailConsumerConfig.consumerFactory();
        assertEquals(expectedConsumerFactory, actual);
    }

    @Test
    public void testValidConsumerConfig() {
        Map<String, Object> expectedConsumerConfig = new HashMap<>();
        expectedConsumerConfig.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapService);
        expectedConsumerConfig.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        expectedConsumerConfig.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        Map<String, Object> actualConsumerConfig = emailConsumerConfig.consumerConfig();

        assertEquals(expectedConsumerConfig, actualConsumerConfig);
    }

    @Test
    public void testValidContainerFactory() {
        KafkaListenerContainerFactory<
                ConcurrentMessageListenerContainer<String, NotificationData>> actual =
                emailConsumerConfig.factory(emailConsumerConfig.consumerFactory());
        assertEquals(expectedContainerFactory, actual);
    }
}

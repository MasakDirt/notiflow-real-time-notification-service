package com.proj.telegramkafka.config;

import com.proj.telegramkafka.model.NotificationData;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Configuration
public class TelegramConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapService;

    @Bean
    public ConsumerFactory<String, NotificationData> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapService);
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return props;
    }

    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, NotificationData>> factory(ConsumerFactory<String, NotificationData> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, NotificationData> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}

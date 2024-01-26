package com.proj.telegramkafka.config;

import com.proj.telegramkafka.model.NotificationData;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class TelegramProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private  String bootstrapService;

    @Bean
    public ProducerFactory<String, NotificationData> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapService);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, NotificationData> kafkaTemplate(
            ProducerFactory<String, NotificationData> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}

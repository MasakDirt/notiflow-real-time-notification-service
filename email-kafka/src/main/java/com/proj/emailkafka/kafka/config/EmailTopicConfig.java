package com.proj.emailkafka.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class EmailTopicConfig {

    @Bean
    public NewTopic emailTopic() {
        return TopicBuilder.name("email").build();
    }
}

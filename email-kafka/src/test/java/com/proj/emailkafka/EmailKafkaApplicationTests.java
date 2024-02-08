package com.proj.emailkafka;

import com.proj.emailkafka.controller.MessageController;
import com.proj.emailkafka.email.EmailSender;
import com.proj.emailkafka.kafka.config.EmailConsumerConfig;
import com.proj.emailkafka.kafka.config.EmailProducerConfig;
import com.proj.emailkafka.kafka.config.EmailTopicConfig;
import com.proj.emailkafka.kafka.listeners.EmailListeners;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailKafkaApplicationTests {

    private final MessageController messageController;
    private final EmailSender emailSender;
    private final EmailConsumerConfig emailConsumerConfig;
    private final EmailProducerConfig emailProducerConfig;
    private final EmailTopicConfig emailTopicConfig;
    private final EmailListeners emailListeners;

    @Autowired
    public EmailKafkaApplicationTests(MessageController messageController, EmailSender emailSender,
                                      EmailConsumerConfig emailConsumerConfig, EmailProducerConfig emailProducerConfig,
                                      EmailTopicConfig emailTopicConfig, EmailListeners emailListeners) {
        this.messageController = messageController;
        this.emailSender = emailSender;
        this.emailConsumerConfig = emailConsumerConfig;
        this.emailProducerConfig = emailProducerConfig;
        this.emailTopicConfig = emailTopicConfig;
        this.emailListeners = emailListeners;
    }

    @Test
    public void testInjectedComponents() {
        AssertionsForClassTypes.assertThat(messageController).isNotNull();
        AssertionsForClassTypes.assertThat(emailSender).isNotNull();
        AssertionsForClassTypes.assertThat(emailConsumerConfig).isNotNull();
        AssertionsForClassTypes.assertThat(emailProducerConfig).isNotNull();
        AssertionsForClassTypes.assertThat(emailTopicConfig).isNotNull();
        AssertionsForClassTypes.assertThat(emailListeners).isNotNull();
    }
}

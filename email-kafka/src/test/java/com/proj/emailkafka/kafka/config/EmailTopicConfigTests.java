package com.proj.emailkafka.kafka.config;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailTopicConfigTests {

    private final EmailTopicConfig emailTopicConfig;

    @Autowired
    public EmailTopicConfigTests(EmailTopicConfig emailTopicConfig) {
        this.emailTopicConfig = emailTopicConfig;
    }

    @Test
    public void testNotNullTelegramTopic() {
        AssertionsForClassTypes.assertThat(emailTopicConfig.emailTopic()).isNotNull();
    }

    @Test
    public void testTopicName() {
        String expectedTopicName = "email";
        String actualTopicName = emailTopicConfig.emailTopic().name();
        Assertions.assertEquals(expectedTopicName, actualTopicName);
    }
}

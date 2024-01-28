package com.proj.telegramkafka.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import javax.validation.constraints.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NotificationData {

    @NotNull(message = "The telegram account must started with '@' character")
    @Pattern(regexp = "^@.+", message = "The telegram account must started with '@' character")
    private String recipientUserTelegram;

    private String senderUserName;

    @NotEmpty(message = "Message must not be empty!")
    private String message;

    public static NotificationData fromJsonString(String jsonData) {
        try {
            return new ObjectMapper().readValue(jsonData, NotificationData.class);
        } catch (Exception e) {
            throw new RuntimeException("JsonData isn`t appropriate to notification data");
        }
    }
}
